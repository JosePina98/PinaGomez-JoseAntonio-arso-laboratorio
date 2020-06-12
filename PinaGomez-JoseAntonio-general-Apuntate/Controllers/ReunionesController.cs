using PinaGomez_JoseAntonio_general_Apuntate.Modelo;
using PinaGomez_JoseAntonio_general_Apuntate.Servicios;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using PinaGomez_JoseAntonio_general_Apuntate.RabbitMQ;
using System;

namespace PinaGomez_JoseAntonio_general_Apuntate.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ReunionesController : ControllerBase
    {
        private readonly ApuntateServicio _apuntateServicio;

        public ReunionesController(ApuntateServicio apuntateServicio)
        {
            _apuntateServicio = apuntateServicio;
        }

        [HttpGet]
        public ActionResult<List<Reunion>> Get() =>
            _apuntateServicio.Get();

        [HttpGet("{id:length(24)}", Name = "GetReunion")]
        public ActionResult<Reunion> Get(string id)
        {
            var reunion = _apuntateServicio.Get(id);

            if (reunion == null)
            {
                return NotFound();
            }

            return reunion;
        }

        private string comprobarErrores(Reunion reunion) {
            if (reunion.titulo == null || reunion.titulo.Equals("")) {
                return "El parámetro titulo no puede ser vacio";
            }
            if (reunion.docenteId == null || reunion.docenteId.Equals("")) {
                return "El parámetro docenteId no puede ser vacio";
            }
            if (reunion.ubicacion == null || reunion.ubicacion.Equals("")) {
                return "El parámetro ubicacion no puede ser vacio";
            }
            if (reunion.frecuencia == null || reunion.frecuencia.Equals("")) {
                return "El parámetro frecuencia no puede ser vacio";
            }
            if (reunion.fechaFin <= reunion.fechaInicio) {
                return "La fecha de fin de la reunion no puede ser anterior a la de inicio";
            }
            long now = DateTimeOffset.Now.ToUnixTimeMilliseconds();
            if (reunion.fechaInicio < now) {
                return "La fecha de inicio de la reunion no puede ser anterior a la fecha actual";
            }
            if (reunion.aperturaInscripcion < 0) { 
                return "El parámetro aperturaInscripcion no puede ser menor de 0";
            }
            if (reunion.cierreInscripcion <= 0) {
                return "El parámetro cierreInscripcion no puede ser menor o igual que 0";
            }
            if (reunion.aperturaInscripcion < reunion.cierreInscripcion) {
                return "El parámetro aperturaInscripcion no puede ser menor que el parámetro cierreInscripcion";
            }
            if (reunion.numPlazas <= 0) {
                return "El parámetro numPlazas no puede ser menor o igual que 0";
            }
            if (reunion.participantesPorIntervalo <= 0) {
                return "El parámetro numPlazas no puede ser menor o igual que 0";
            }
            return "";
        }

        [HttpPost]
        public ActionResult<Reunion> Create(Reunion reunion)
        {
            string mensaje = comprobarErrores(reunion);
            if (!mensaje.Equals("")) {
                return BadRequest(mensaje);
            }
            
            bool resultado = UsuariosServicio.existeDocente(reunion.docenteId);
            if (!resultado) {
                return BadRequest("El id del docente no es correcto.");
            }

            _apuntateServicio.Create(reunion);

            try {
                Productor.notificarEventoNuevaReunion(reunion);
            } catch (Exception) {
                return StatusCode(500);
            }

            return CreatedAtRoute("GetReunion", new { id = reunion.Id.ToString() }, reunion);
        }

        
        [HttpPost("{id:length(24)}/addParticipante")]
        public IActionResult addParticipante(string id, Inscripcion cita)
        {
            bool resultado = UsuariosServicio.existeEstudiante(cita.alumnoId);

            if (!resultado) {
                return BadRequest("El id del estudiante no es correcto.");
            }

            var reunion = _apuntateServicio.Get(id);

            if (reunion == null)
            {
                return NotFound();
            }

            resultado = reunion.addInscripcion(cita);

            if (resultado) {
                _apuntateServicio.Update(id, reunion);

                try {
                    Productor.notificarEventoInscripcion(reunion, cita.alumnoId);
                    return NoContent();
                } catch (Exception) {
                    return StatusCode(500);
                }
            } else {
                return BadRequest("No se puede añadir esta cita.");
            }

        }

        [HttpPost("{id:length(24)}/removeParticipante/{correoAlumno}")]
        public IActionResult removeParticipante(string id, string correoAlumno)
        {
            bool resultado = UsuariosServicio.existeEstudiante(correoAlumno);

            if (!resultado) {
                return BadRequest("El id del estudiante no es correcto.");
            }

            var reunion = _apuntateServicio.Get(id);

            if (reunion == null)
            {
                return NotFound();
            }

            resultado = reunion.removeInscripcion(correoAlumno);

            if (resultado) {
                _apuntateServicio.Update(id, reunion);
                return NoContent();
            } else {
                return BadRequest("El participante no tiene cita.");
            }
        }

        [HttpDelete("{id:length(24)}")]
        public IActionResult Delete(string id)
        {
            var reunion = _apuntateServicio.Get(id);

            if (reunion == null)
            {
                return NotFound();
            }

            _apuntateServicio.Remove(reunion.Id);

            return NoContent();
        }
    }
}