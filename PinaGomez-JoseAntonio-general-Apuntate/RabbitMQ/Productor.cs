using System;
using RabbitMQ.Client;
using System.Text;
using PinaGomez_JoseAntonio_general_Apuntate.Modelo;

namespace PinaGomez_JoseAntonio_general_Apuntate.RabbitMQ
{
    public class Productor
    {
        private static void enviarMensaje(string mensaje)
        {
            var factory = new ConnectionFactory() { Uri = new Uri("amqp://bawartxa:v9KM0UrC9D8qCJxmRyhClrjJlVOEd8gA@squid.rmq.cloudamqp.com/bawartxa") };
            var connection = factory.CreateConnection();
            var channel = connection.CreateModel();

            channel.ExchangeDeclare(exchange: "arso-exchange",
                                type: "direct",
                                durable: true,
                                autoDelete: false,
                                arguments: null);

            channel.QueueDeclare(queue: "arso-queue",
                                 durable: true,
                                 exclusive: false,
                                 autoDelete: false,
                                 arguments: null);

            channel.QueueBind(queue: "arso-queue",
                                exchange: "arso-exchange",
                                routingKey: "arso-queue");

            var body = Encoding.UTF8.GetBytes(mensaje);

            IBasicProperties props = channel.CreateBasicProperties();
            props.ContentType = "text/plain";

            channel.BasicPublish(exchange: "arso-exchange",
                                 routingKey: "arso-queue",
                                 basicProperties: props,
                                 body: body);

            channel.Close();
            connection.Close();
        }
        public static void notificarEventoNuevaReunion(Reunion reunion) {
            
            string mensaje = "{ \"tipo\": \"new\", \"profesor\" : \"" + reunion.docenteId + "\", \"idTarea\" : \"" + reunion.Id +
				"\", \"servicio\" : \"apuntate\" }";

            try {
                enviarMensaje(mensaje);
            } catch (Exception e) {
                throw new Exception("Error interno del sistema: " + e);
            }
        }

        public static void notificarEventoInscripcion(Reunion reunion, string idEstudiante) {
            
            string mensaje = "{ \"tipo\": \"remove\", \"profesor\" : \"" + reunion.docenteId + "\", \"idTarea\" : \"" + reunion.Id +
				"\", \"servicio\" : \"apuntate\", \"estudiante\" : \"" + idEstudiante + "\" }";

            try {
                enviarMensaje(mensaje);
            } catch (Exception e) {
                throw new Exception("Error interno del sistema: " + e);
            }
        }
    }

}