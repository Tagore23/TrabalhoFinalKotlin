Desenvolvimento de Aplicativos Móveis - 23/11/2024
Eduardo Zaduski, Gabriel Braga, Tagore Nataniel

Este é um aplicativo desenvolvido em Kotlin para o gerenciamento de receitas culinárias, permitindo que usuários se cadastrem, façam login, e gerenciem suas receitas pessoais. O sistema oferece funcionalidades para visualizar, adicionar, editar, e excluir receitas. O projeto utiliza o banco de dados local Room para armazenar os dados do usuário e suas receitas, garantindo persistência mesmo quando o aplicativo é fechado. A navegação entre as telas é estruturada de forma a proporcionar uma experiência de usuário intuitiva e prática.

Funcionalidades
O aplicativo possui as seguintes funcionalidades principais:

Tela de Login/Registro: Permite que o usuário crie uma conta ou faça login utilizando seu e-mail e senha.
Tela de Receitas: Exibe uma lista de receitas relacionadas à categoria selecionada, podendo ler ali a receita, editar ou excluir.
Armazenamento Local (Room): Todas as receitas são armazenadas localmente, e cada receita é associada ao usuário logado.


Banco de Dados - Room

O aplicativo utiliza o Room como banco de dados local para armazenar as informações dos usuários e suas receitas. As entidades e o relacionamento entre elas são descritos a seguir:

Entidades:
Usuário

id: Identificador único do usuário (chave primária).
nome: Nome do usuário.
email: E-mail do usuário.
senha: Senha do usuário (armazenada de forma segura).

Receita

id: Identificador único da receita (chave primária).
titulo: Título da receita.
descricao: Descrição da receita.
categoria: Categoria da refeição
dataCriacao: Data de criação da receita.
usuarioId: Chave estrangeira associando a receita ao usuário (relacionamento N:1 com Usuário).

Relacionamentos:

Usuário 1:N Receita: Um usuário pode ter várias receitas, mas cada receita pertence a um único usuário.

Descrição das Activities

Tela de Login/Registro:

O usuário pode fazer login ou se registrar.
Campos necessários: e-mail e senha.

Tela de Receitas:

O usuário pode adicionar uma nova receita ou editar/excluir receitas existentes.
Exibe as informações detalhadas de uma receita, incluindo título e descrição.
O usuário pode editar ou excluir a receita.
Permite ao usuário adicionar uma nova receita, preenchendo o título e a descrição e a categoria.
O usuário pode editar uma receita existente, alterando o título e/ou a descrição e/ou a categoria.
O usuário pode excluir uma receita após confirmação.
Também pode realizar logout.