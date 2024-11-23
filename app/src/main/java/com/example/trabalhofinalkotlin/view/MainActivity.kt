package com.example.trabalhofinalkotlin.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.trabalhofinalkotlin.viewmodel.ReceitasViewModelFactory
import com.example.trabalhofinalkotlin.model.database.AppDataBase
import com.example.trabalhofinalkotlin.model.entity.Receitas
import com.example.trabalhofinalkotlin.model.entity.Usuario
import com.example.trabalhofinalkotlin.viewmodel.ReceitasViewModel
import com.example.trabalhofinalkotlin.viewmodel.UsuarioViewModel
import com.example.trabalhofinalkotlin.viewmodel.UsuarioViewModelFactory

class MainActivity : ComponentActivity() {
    private val receitasViewModel: ReceitasViewModel by viewModels {
        val dao = AppDataBase.getDatabase(applicationContext).receitasDao()
        ReceitasViewModelFactory(dao, applicationContext) // Passa o applicationContext
    }


    private val usuarioViewModel: UsuarioViewModel by viewModels {
        val dao = AppDataBase.getDatabase(applicationContext).usuarioDao()
        UsuarioViewModelFactory(dao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Chamando a MainScreen com o NavController
            MainScreen(receitasViewModel = receitasViewModel, usuarioViewModel = usuarioViewModel)
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    receitasViewModel: ReceitasViewModel,
    usuarioViewModel: UsuarioViewModel
) {
    // Navegação
    val navController = rememberNavController()

    // Componente de Navegação
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController, usuarioViewModel = usuarioViewModel, receitasViewModel = receitasViewModel)
        }
        composable("cadastro") {
            CadastroScreen(usuarioViewModel = usuarioViewModel, navController = navController)
        }
        composable("receitas/{usuarioId}") { backStackEntry ->
            val usuarioId = backStackEntry.arguments?.getString("usuarioId")?.toIntOrNull() ?: 0
            ReceitasScreen(receitasViewModel = receitasViewModel, navController = navController, usuarioId = usuarioId)
        }
    }
}

// Tela de Login
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    usuarioViewModel: UsuarioViewModel,
    receitasViewModel: ReceitasViewModel,
    navController: NavHostController
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Login")

        Text("E-mail")
        TextField(value = email, onValueChange = { email = it }, modifier = Modifier.fillMaxWidth())

        Text("Senha")
        TextField(value = senha, onValueChange = { senha = it }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
                usuarioViewModel.loginUsuario(email, senha) { usuario ->
                    if (usuario != null) {
                        Toast.makeText(context, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()

                        // Atualiza as receitas do usuário logado
                        receitasViewModel.buscarReceitasPorUsuario(usuario.usuId)

                        // Navega para a tela de receitas
                        navController.navigate("receitas/${usuario.usuId}")
                    } else {
                        Toast.makeText(context, "E-mail ou senha inválidos", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Entrar")
        }



        TextButton(onClick = { navController.navigate("cadastro") }) {
            Text("Ainda não tem uma conta? Cadastre-se")
        }
    }
}

// Tela de Cadastro
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroScreen(
    usuarioViewModel: UsuarioViewModel,
    navController: NavHostController
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmacaoSenha by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Cadastro")

        Text("Nome")
        TextField(value = nome, onValueChange = { nome = it }, modifier = Modifier.fillMaxWidth())

        Text("E-mail")
        TextField(value = email, onValueChange = { email = it }, modifier = Modifier.fillMaxWidth())

        Text("Senha")
        TextField(value = senha, onValueChange = { senha = it }, modifier = Modifier.fillMaxWidth())

        Text("Confirmar Senha")
        TextField(value = confirmacaoSenha, onValueChange = { confirmacaoSenha = it }, modifier = Modifier.fillMaxWidth())

        if (mensagemErro.isNotEmpty()) {
            Text(mensagemErro, color = androidx.compose.ui.graphics.Color.Red)
        }

        Button(
            onClick = {
                usuarioViewModel.registrarUsuario(nome, email, senha, confirmacaoSenha) { sucesso, mensagem ->
                    if (sucesso) {
                        Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show()
                        navController.navigate("login")
                    } else {
                        mensagemErro = mensagem ?: "Erro desconhecido"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Cadastrar")
        }
    }
}


// Tela de Receitas
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceitasScreen(
    receitasViewModel: ReceitasViewModel,
    navController: NavController,
    usuarioId: Int
) {
    val receitas by receitasViewModel.receitasLiveData.observeAsState(emptyList())

    // Estados para gerenciar o formulário
    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var receitaToEdit by remember { mutableStateOf<Receitas?>(null) } // Receita sendo editada ou null para criação

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Receitas", modifier = Modifier.padding(bottom = 16.dp))

        // Formulário de criação/edição
        TextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        TextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoria") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        Button(
            onClick = {
                if (titulo.isNotEmpty() && descricao.isNotEmpty() && categoria.isNotEmpty()) {
                    receitaToEdit?.let { receita ->
                        // Atualizar receita existente
                        receitasViewModel.editarReceita(
                            id = receita.id,
                            titulo = titulo,
                            descricao = descricao,
                            usuarioId = usuarioId
                        )
                        Toast.makeText(context, "Receita atualizada!", Toast.LENGTH_SHORT).show()
                    } ?: run {
                        // Criar nova receita
                        receitasViewModel.adicionarReceita(
                            titulo = titulo,
                            descricao = descricao,
                            categoria = categoria,
                            usuarioId = usuarioId
                        )
                        Toast.makeText(context, "Receita criada!", Toast.LENGTH_SHORT).show()
                    }
                    // Limpar formulário e estado de edição
                    titulo = ""
                    descricao = ""
                    categoria = ""
                    receitaToEdit = null
                } else {
                    Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text(if (receitaToEdit != null) "Atualizar Receita" else "Salvar Receita")
        }

        // Lista de receitas
        LazyColumn(modifier = Modifier.weight(1f).padding(top = 16.dp)) {
            items(receitas) { receita ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("${receita.titulo} - ${receita.descricao}")
                        Text("Categoria: ${receita.categoria}")
                    }
                    Button(
                        onClick = {
                            // Preencher formulário com os dados da receita para edição
                            receitaToEdit = receita
                            titulo = receita.titulo
                            descricao = receita.descricao
                            categoria = receita.categoria
                        },
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .width(100.dp)
                            .padding(horizontal = 4.dp)
                    ) {
                        Text("Editar")
                    }
                    // Botão de Excluir
                    Button(
                        onClick = {
                            // Excluir a receita e atualizar a lista
                            receitasViewModel.excluirReceita(receita.id, usuarioId)
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .width(100.dp)
                            .padding(horizontal = 4.dp)
                    ) {
                        Text("Excluir")
                    }
                }
            }
        }

        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Logout")
        }
    }
}







