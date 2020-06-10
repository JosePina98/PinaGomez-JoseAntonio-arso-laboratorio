using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace PinaGomez_JoseAntonio_general_Apuntate.Modelo
{
    public class Reunion
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }

        [BsonElement("Name")]
        public string BookName { get; set; }

    }
}