create table forma_pagamento (
id bigint not null auto_increment, 
descricao varchar(80), 
primary key (id)) 
engine=InnoDB DEFAULT CHARSET=utf8;

create table grupo (
id bigint not null auto_increment, 
nome varchar(255), 
primary key (id)
) engine=InnoDB DEFAULT CHARSET=utf8;

create table grupo_permissao (
grupo_id bigint not null, 
permissao_id bigint not null
) engine=InnoDB DEFAULT CHARSET=utf8;

create table permissao (
id bigint not null auto_increment, 
descricao varchar(80), 
nome varchar(50), 
primary key (id)
) engine=InnoDB DEFAULT CHARSET=utf8;

create table produto (
id bigint not null auto_increment, 
ativo bit not null, 
descricao varchar(150), 
nome varchar(100), 
preco decimal(19,2), 
restaurante_id bigint, primary key (id)
) engine=InnoDB DEFAULT CHARSET=utf8;

create table restaurante (
id bigint not null auto_increment, 
data_atualizacao datetime not null, 
data_cadastro datetime not null, 
endereco_bairro varchar(100), 
endereco_cep varchar(8), 
endereco_complemento varchar(80), 
endereco_logradouro varchar(150), 
endereco_numero varchar(80), 
nome varchar(150), 
taxa_frete decimal(19,2), 
cozinha_id bigint, 
endereco_cidade_id bigint, 
primary key (id)
) engine=InnoDB DEFAULT CHARSET=utf8;

create table restaurante_forma_pagamento (
restaurante_id bigint not null, 
forma_pagamento_id bigint not null
) engine=InnoDB DEFAULT CHARSET=utf8;

create table usuario (
id bigint not null auto_increment, 
data_cadastro datetime, 
email varchar(100), 
nome varchar(150), 
senha varchar(50), 
primary key (id)
) engine=InnoDB DEFAULT CHARSET=utf8;

create table usuario_grupo (
usuario_id bigint not null, 
grupo_id bigint not null
) engine=InnoDB DEFAULT CHARSET=utf8;

alter table grupo_permissao 
add constraint FK_grupo_permissao_permissao 
foreign key (permissao_id) 
references permissao (id) ON DELETE CASCADE;

alter table grupo_permissao 
add constraint FK_grupo_permissao_grupo 
foreign key (grupo_id) 
references grupo (id) ON DELETE CASCADE;

alter table produto 
add constraint FK_produto_restaurante 
foreign key (restaurante_id) 
references restaurante (id) ON DELETE CASCADE;

alter table restaurante 
add constraint FK_restaurante_cozinha
foreign key (cozinha_id) 
references cozinha (id);

alter table restaurante 
add constraint FK_restaurante_cidade
foreign key (endereco_cidade_id) 
references cidade (id) ON DELETE CASCADE;

alter table restaurante_forma_pagamento 
add constraint FK_restaurante_forma_pagamento_forma_pagamento 
foreign key (forma_pagamento_id) 
references forma_pagamento (id) ON DELETE CASCADE;

alter table restaurante_forma_pagamento 
add constraint FK_restaurante_forma_pagamento_restaurante
foreign key (restaurante_id) 
references restaurante (id) ON DELETE CASCADE;

alter table usuario_grupo 
add constraint FK_usuario_grupo_grupo
foreign key (grupo_id) 
references grupo (id) ON DELETE CASCADE;

alter table usuario_grupo 
add constraint FK_usuario_grupo_usuario
foreign key (usuario_id) 
references usuario (id) ON DELETE CASCADE;
