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
        private readonly ApuntateService _apuntateService;

        public ReunionesController(ApuntateService apuntateService)
        {
            _apuntateService = apuntateService;
        }

        [HttpGet]
        public ActionResult<List<Reunion>> Get() =>
            _apuntateService.Get();

        [HttpGet("{id:length(24)}", Name = "GetReunion")]
        public ActionResult<Reunion> Get(string id)
        {
            var reunion = _apuntateService.Get(id);

            if (reunion == null)
            {
                return NotFound();
            }

            return reunion;
        }

        [HttpPost]
        public ActionResult<Reunion> Create(Reunion reunion)
        {
            _apuntateService.Create(reunion);

            return CreatedAtRoute("GetReunion", new { id = reunion.Id.ToString() }, reunion);
        }

        
        [HttpPost("{id:length(24)}/addParticipante")]
        public IActionResult addParticipante(string id, Cita cita)
        {
            var reunion = _apuntateService.Get(id);

            if (reunion == null)
            {
                return NotFound();
            }

            bool resultado = reunion.addCita(cita);

            if (resultado) {
                _apuntateService.Update(id, reunion);
                return NoContent();
            } else {
                return BadRequest("No se puede añadir esta cita.");
            }

        }

        [HttpPost("{id:length(24)}/removeParticipante")]
        public IActionResult removeParticipante(string id, string alumnoId)
        {
            var reunion = _apuntateService.Get(id);

            if (reunion == null)
            {
                return NotFound();
            }

            bool resultado = reunion.removeCita(alumnoId);

            if (resultado) {
                _apuntateService.Update(id, reunion);
                return NoContent();
            } else {
                return BadRequest("El participante no tiene cita.");
            }
        }

        [HttpDelete("{id:length(24)}")]
        public IActionResult Delete(string id)
        {
            var reunion = _apuntateService.Get(id);

            if (reunion == null)
            {
                return NotFound();
            }

            _apuntateService.Remove(reunion.Id);

            return NoContent();
        }
    }
}