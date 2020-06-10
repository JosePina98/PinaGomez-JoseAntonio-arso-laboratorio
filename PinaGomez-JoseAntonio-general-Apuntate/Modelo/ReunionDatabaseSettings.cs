namespace PinaGomez_JoseAntonio_general_Apuntate.Modelo
{
    public class ReunionDatabaseSettings : IReunionDatabaseSettings
    {
        public string ReunionesCollectionName { get; set; }
        public string ConnectionString { get; set; }
        public string DatabaseName { get; set; }
    }

    public interface IReunionDatabaseSettings
    {
        string ReunionesCollectionName { get; set; }
        string ConnectionString { get; set; }
        string DatabaseName { get; set; }
    }
}