using PinaGomez_JoseAntonio_general_Apuntate.Modelo;
using PinaGomez_JoseAntonio_general_Apuntate.Servicios;
using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;

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

        [HttpPost]
        public ActionResult<Reunion> Create(Reunion reunion)
        {
            bool resultado = UsuariosServicio.existeDocente(reunion.docenteId);

            if (!resultado) {
                return BadRequest("El id del docente no es correcto.");
            }

            _apuntateServicio.Create(reunion);

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

            resultado = reunion.addCita(cita);

            if (resultado) {
                _apuntateServicio.Update(id, reunion);
                return NoContent();
            } else {
                return BadRequest("No se puede añadir esta cita.");
            }

        }

        [HttpPost("{id:length(24)}/removeParticipante/{alumnoId}")]
        public IActionResult removeParticipante(string id, string alumnoId)
        {
            bool resultado = UsuariosServicio.existeEstudiante(alumnoId);

            if (!resultado) {
                return BadRequest("El id del estudiante no es correcto.");
            }

            var reunion = _apuntateServicio.Get(id);

            if (reunion == null)
            {
                return NotFound();
            }

            resultado = reunion.removeCita(alumnoId);

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