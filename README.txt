Integrantes:
- Francisco Abarca 201673552-6
- Juan Escalona 201373551-7

!! Para la compilacion basta con hacer -> make ftp
!! Para iniciar el servidor esclavo se necesita hacer -> make run_server
!! Para iniciar el servidor intermedio se necesita hacer -> make run_server_partes
!! Para iniciar un cliente se debe hacer -> make run_cliente
!! Para limpiar los archivos innecesarios hacer -> make clean
!! Para eliminar el log -> make clean_log
!! Los archivos del lado del cliente estan en -> ./Client
!! Los archivos del lado del servidor estan en -> ./Server
!! Los verbos aceptados son: ls, del, get, put los cuales son sensibles a su capitalizacion
!! El puerto usado para el servidor es el 59090 verificar que no este siendo usado
!! Se hace uso de localhost como direccion para el servidor
!! El sistemas de archivos esta pensado en Unix-Based (/ no \)
