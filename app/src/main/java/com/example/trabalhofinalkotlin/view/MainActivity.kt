package com.example.trabalhofinalkotlin.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.trabalhofinalkotlin.model.entity.Usuario
import com.example.trabalhofinalkotlin.viewmodel.ReceitasViewModel
import com.example.trabalhofinalkotlin.viewmodel.UsuarioViewModel
import com.example.trabalhofinalkotlin.viewmodel.UsuarioViewModelFactory

class MainActivity : ComponentActivity() {
    private val receitasViewModel: ReceitasViewModel by viewModels {
        val dao = AppDataBase.getDatabase(applicationContext).receitasDao()
        ReceitasViewModelFactory(dao)
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
            LoginScreen(navController = navController, usuarioViewModel = usuarioViewModel)
        }
        composable("cadastro") {
            CadastroScreen(usuarioViewModel = usuarioViewModel, navController = navController)
        }
        composable("receitas") {
            ReceitasScreen(receitasViewModel = receitasViewModel, navController = navController)
        }
    }
}

// Tela de Login
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    usuarioViewModel: UsuarioViewModel,
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
                        navController.navigate("receitas")
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
) {
    val receitas by receitasViewModel.receitasLiveData.observeAsState(emptyList())
    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }

    // Supondo que você tenha o usuário logado
    val usuarioId = remember { mutableStateOf(0) } // Inicializa com 0
    val context = LocalContext.current // Aqui pegamos o contexto para usar no Toast

    // Suponha que você tenha o usuário logado e o id seja armazenado
    val usuarioLogado = remember { mutableStateOf<Usuario?>(null) }

    // Quando o usuário fizer login, deve definir o `usuarioId`
    LaunchedEffect(usuarioLogado.value) {
        if (usuarioLogado.value != null) {
            usuarioId.value = usuarioLogado.value?.usuId ?: 0
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Receitas")

        // Formulário para adicionar nova receita
        TextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
        TextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição") }, modifier = Modifier.fillMaxWidth())
        TextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoria") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
                if (titulo.isNotEmpty() && descricao.isNotEmpty() && categoria.isNotEmpty() && usuarioId.value != 0) {
                    receitasViewModel.adicionarReceita(titulo, descricao, categoria, usuarioId.value)
                    titulo = ""
                    descricao = ""
                    categoria = ""
                } else {
                    // Aqui chamamos o Toast corretamente
                    Toast.makeText(context, "Preencha todos os campos e faça login.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Adicionar Receita")
        }

        LazyColumn(modifier = Modifier.fillMaxHeight().padding(top = 16.dp)) {
            items(receitas) { receita ->
                Text("${receita.titulo} - ${receita.descricao}", modifier = Modifier.fillMaxWidth().padding(8.dp))
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


