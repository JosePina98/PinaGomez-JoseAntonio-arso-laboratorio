using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace PinaGomez_JoseAntonio_general_Apuntate.Modelo
{
    public class Cita
    {
        public string alumnoId { get; set; }
        public int intervalo { get; set; }
        public string comentario { get; set; }
    }
}