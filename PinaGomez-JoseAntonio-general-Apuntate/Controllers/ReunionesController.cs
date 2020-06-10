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

        [HttpPut("{id:length(24)}")]
        public IActionResult Update(string id, Reunion reunionIn)
        {
            var reunion = _apuntateService.Get(id);

            if (reunion == null)
            {
                return NotFound();
            }

            _apuntateService.Update(id, reunionIn);

            return NoContent();
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