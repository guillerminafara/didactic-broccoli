use ProyectoFinal;
create table usuario (
    id_usuario int primary key auto_increment,
    nombre varchar(30) not null,
    apellido varchar(30) not null,
    email varchar(30) not null unique,
    contrasena varchar(30) not null,
);
insert into usuario (nombre, apellido, email, contrasena) values
('Juan', 'Perez', 'juanperez@mail.com', 'juan123');

create table administrador (
    id_administrador int primary key auto_increment,
  email varchar(30) not null unique,
    contrasena varchar(30) not null
);
insert into administrador (email, contrasena) values
('admin@admin.com', 'admin123');

create table compra(
    id_compra int primary key auto_increment,
    seguimiento varchar(20) unique not null,
    id_usuario int,
    fecha_compra date not null default current_timestamp,
    total decimal(10,2) not null,
    foreign key (id_usuario) references usuario(id_usuario)
);


create table productos (
    id_producto int primary key auto_increment,
    nombre varchar(50) not null,
    descripcion varchar(255),
    imagen varchar(500),
    precio decimal(10,2) not null  
);
create table compra_producto (
    id_compra_producto int primary key auto_increment,
    id_producto int,
    id_compra int,
    cantidad int not null, default 1,
    precio_unitario decimal(10,2) not null,
   subtotal decimal(10,2) not null as cantidad * precio_unitario,
    foreign key (id_compra) references compra(id_compra),
    foreign key (id_producto) references producto(id_producto)
);

insert into producto (nombre, descripcion, precio) values
('Canela de Ceylán', 'Canela en polvo 80g Bio Cesta', 5.35),
('Jengibre en polvo', 'Jengibre en polvo 80g Bio Cesta', 3.49),
('Cúrcuma en polvo', 'Cúrcuma en polvo 80g Bio Cesta', 5.19),
('Pimentón Ducle', 'Pimentón Dulce 38g Artemis Bio', 2.85),
('Comino molido', 'Comino molido 80g Bio Cesta', 3.19),
('Curry en polvo', 'Curry en polvo 80g Vegetalia', 5.10);

