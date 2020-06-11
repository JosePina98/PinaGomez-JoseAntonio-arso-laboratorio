using System.Collections.Generic;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;

namespace PinaGomez_JoseAntonio_general_Apuntate.Modelo
{
    public class Reunion
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }
        public string titulo { get; set; }
        public string docenteId { get; set; }
        public string ubicacion { get; set; }
        public long fechaInicio { get; set; }
        public long fechaFin { get; set; }
        public string frecuencia { get; set; }
        public int aperturaInscripcion { get; set; }
        public int cierreInscripcion { get; set; }
        public int numPlazas { get; set; }
        public int participantesPorIntervalo { get; set; }
        public List<Inscripcion> listaInscripciones { get; set; }

        public int getTiempoPorParticipante() {
            long diferencia = fechaFin - fechaInicio;

            int diferencia_minutos = (int) (diferencia / 60000);

            int numTurnos = (numPlazas + participantesPorIntervalo - 1) / participantesPorIntervalo;

            if (diferencia_minutos < numTurnos) {
                return 1;
            } else {
                return (diferencia_minutos / numTurnos);
            }
        }

        private bool turnoLibre(int intervalo) {
            long diferencia = fechaFin - fechaInicio;
            int diferencia_minutos = (int) (diferencia / 60000);
            int maxTurnos = diferencia_minutos / getTiempoPorParticipante();
            if (intervalo > maxTurnos) {
                return false;
            }

            int yaReservados = 0;
            foreach (Inscripcion cita in listaInscripciones) {
                if (cita.intervalo == intervalo) {
                    yaReservados++;
                }
            }

            if (yaReservados >= participantesPorIntervalo) {
                return false;
            } else {
                return true;
            }
        }

        private bool yaReservado(String alumnoId) {
            foreach (Inscripcion cita in listaInscripciones) {
                if (cita.alumnoId == alumnoId) {
                    return true;
                }
            }

            return false;
        }

        public bool addCita(Inscripcion cita) {
            if (this.listaInscripciones == null) {
                this.listaInscripciones = new List<Inscripcion>();
            }

            if (!turnoLibre(cita.intervalo) || yaReservado(cita.alumnoId)) {
                return false;
            }

            if (this.listaInscripciones.Contains(cita)) {
                return false;
            } else {
                this.listaInscripciones.Add(cita);
                return true;
            }
        }

        public bool removeCita(String alumonoId) {
            if (this.listaInscripciones == null) {
                this.listaInscripciones = new List<Inscripcion>();
            }

            for (int i = 0; i < listaInscripciones.Count; i++) {
                if (listaInscripciones[i].alumnoId == alumonoId) {
                    listaInscripciones.RemoveAt(i);
                    return true;
                }
            }
            return false;
        }
    }
}