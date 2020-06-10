using PinaGomez_JoseAntonio_general_Apuntate.Modelo;
using MongoDB.Driver;
using System.Collections.Generic;
using System.Linq;

namespace PinaGomez_JoseAntonio_general_Apuntate.Servicios
{
    public class ApuntateService
    {
        private readonly IMongoCollection<Reunion> _reuniones;

        public ApuntateService(IReunionDatabaseSettings settings)
        {
            var client = new MongoClient("mongodb+srv://arso:arso-20@cluster0-xditi.azure.mongodb.net/<dbname>?retryWrites=true&w=majority");

            var database = client.GetDatabase("arso");

            _reuniones = database.GetCollection<Reunion>("reuniones");
        }

        public List<Reunion> Get() =>
            _reuniones.Find(reunion => true).ToList();

        public Reunion Get(string id) =>
            _reuniones.Find<Reunion>(reunion => reunion.Id == id).FirstOrDefault();

        public Reunion Create(Reunion reunion)
        {
            _reuniones.InsertOne(reunion);
            return reunion;
        }

        public void Update(string id, Reunion reunionIn) =>
            _reuniones.ReplaceOne(reunion => reunion.Id == id, reunionIn);

        public void Remove(Reunion reunionIn) =>
            _reuniones.DeleteOne(reunion => reunion.Id == reunionIn.Id);

        public void Remove(string id) => 
            _reuniones.DeleteOne(book => book.Id == id);
    }
}