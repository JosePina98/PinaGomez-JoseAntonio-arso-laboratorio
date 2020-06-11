using PinaGomez_JoseAntonio_general_Apuntate.Modelo;
using MongoDB.Driver;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System;
using System.Net;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using Newtonsoft.Json.Linq;

namespace PinaGomez_JoseAntonio_general_Apuntate.Servicios
{
    public class UsuariosServicio
    {
        private static string URL_SERVICIO = "http://localhost:8081/api/usuarios/";
        private static string ROL_PROFESOR = "PROFESOR";
        private static string ROL_ESTUDIANTE = "ESTUDIANTE";
        private static HttpClient client = new HttpClient();

        private async static Task<string> enviarPeticion(string peticion) {
            HttpResponseMessage responseMessage = await client.GetAsync(peticion);
            if (responseMessage.IsSuccessStatusCode) {
                string responseString = await responseMessage.Content.ReadAsStringAsync();
                return responseString;
            }
            return null;
        }

        public static bool existeDocente(string docenteId) {
            string peticion = URL_SERVICIO + docenteId;

            var respuesta = enviarPeticion(peticion).GetAwaiter().GetResult();

            if (respuesta == null) {
                return false;
            }

            string respuestaString = respuesta.ToString();

            JObject json = JObject.Parse(respuestaString);
            string rol = json.GetValue("rol").ToString();

            if (rol.Equals(ROL_PROFESOR)) {
                return true;
            } else {
                return false;
            }
        }

        public static bool existeEstudiante(string alumnoId) {
            string peticion = URL_SERVICIO + alumnoId;

            var respuesta = enviarPeticion(peticion).GetAwaiter().GetResult();

            if (respuesta == null) {
                return false;
            }

            string respuestaString = respuesta.ToString();

            JObject json = JObject.Parse(respuestaString);
            string rol = json.GetValue("rol").ToString();

            if (rol.Equals(ROL_ESTUDIANTE)) {
                return true;
            } else {
                return false;
            }
        }
    }
}