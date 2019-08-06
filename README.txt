Integrantes:
- Francisco Abarca 201673552-6
- Juan Escalona 201373551-7

!! Orden usual IP -> ftp -> run_server_partes -> run_server -> run_cliente
!! Para definir la IP del server usamos make dir='IP' IP 
!! ej: make dir='10.0.0.1'
!! make IP SOLO FUNCIONA una vez, debido al uso de sed para cambiar localhost
!! Se puede usar nuevamente pero se debe cambiar la IP usada actualmente ver Makefile
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
!! El puerto usado por los esclavos es el 59091 verificar que no este siendo usado
!! Se hace uso de localhost como direccion para el servidor
!! El sistemas de archivos esta pensado en Unix-Based (/ no \)
