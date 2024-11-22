insert into Empresas (nome, setor, Localizacao_Geografica, Carbono_Atual) values ('Empresa A', 'Rural', 'São Paulo', 100.00);
insert into Empresas (nome, setor, Localizacao_Geografica, Carbono_Atual) values ('Empresa B', 'Industrial', 'Rio de Janeiro', 80.00);
insert into Empresas (nome, setor, Localizacao_Geografica, Carbono_Atual) values ('Empresa C', 'Tecnologia', 'Curitiba', 90.00);

insert into role(nome) values('Visitante');
insert into role(nome) values('Administrador');
insert into role(nome) values('Especialista Ambiental');

insert into usuario(username,password,nome) values('eduardo@gmail.com','$2a$12$.zEWiuGP9QRx850tXeQvUOGMMRtev4E8IEUpF8tPqalAqF4/OGdam','Eduardo');
insert into usuario(username,password,nome) values('leonardo@gmail.com','$2a$12$iVljKROyqPu/mP8v4c6Us.I6Ed9w12Kubb9Eqfk82TvGJJFCT6Od6','Leonardo');
insert into usuario(username,password,nome) values('joao@gmail.com','$2a$12$8Mljsqj3Kwtk6LiBxCkSfOJHc45AfSugLiqEmlY0WBpKNG0Wdkd1y','Joao');

insert into usuario_roles_associacao(id_role,id_usuario) values(1,1);
insert into usuario_roles_associacao(id_role,id_usuario) values(2,2);
insert into usuario_roles_associacao(id_role,id_usuario) values(3,3);


