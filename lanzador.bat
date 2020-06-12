cd ./PinaGomez-JoseAntonio-general-Apuntate
start /B dotnet run
cd ..

cd ./PinaGomez-JoseAntonio-general-Sondeos
start /B mvn jetty:run
cd ..

cd ./PinaGomez-JoseAntonio-general-Tareas
start /B mvn jetty:run
cd ..

cd ./PinaGomez-JoseAntonio-general-Usuarios
start /B mvn jetty:run
cd ..