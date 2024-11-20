package com.example.trabalhofinalkotlin.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trabalhofinalkotlin.model.entity.Receitas
import com.example.trabalhofinalkotlin.viewmodel.ReceitasViewModel

class MainActivity : ComponentActivity() {
    private val receitasViewModel: ReceitasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(receitasViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(receitasViewModel: ReceitasViewModel) {
    var titulo by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var receitaTemp by remember { mutableStateOf<Receitas?>(null) }
    var textoBotao by remember { mutableStateOf("Salvar") }
    var modoEditar by remember { mutableStateOf(false) }

    // Atualiza a lista de receitas observando o LiveData do ViewModel
    val listaReceitas by receitasViewModel.receitasLiveData.observeAsState(emptyList())

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Variável de estado para exibir ou ocultar a caixa de diálogo
    var mostrarCaixaDialogo by remember { mutableStateOf(false) }

    // Caixa de diálogo para confirmação de exclusão
    if (mostrarCaixaDialogo) {
        ExcluirReceita(onConfirm = {
            receitaTemp?.let {
                receitasViewModel.excluirReceita(it.id)
            }
            mostrarCaixaDialogo = false
        }, onDismiss = { mostrarCaixaDialogo = false })
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Lista de Receitas",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = titulo,
            onValueChange = { titulo = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Título da receita") }
        )

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = descricao,
            onValueChange = { descricao = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Descrição da receita") }
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (modoEditar) {
                    // Atualizar a receita existente
                    receitaTemp?.let {
                        receitasViewModel.editarReceita(it.id, titulo, descricao)
                        modoEditar = false
                        textoBotao = "Salvar"
                    }
                } else {
                    // Salvar nova receita
                    receitasViewModel.adicionarReceita(titulo, descricao, "Almoço", 1) // "Almoço" e 1 são exemplos
                }

                // Limpar campos após salvar/editar
                titulo = ""
                descricao = ""
                focusManager.clearFocus()
            }
        ) {
            Text(text = textoBotao)
        }

        Spacer(modifier = Modifier.height(15.dp))

        // Lista de receitas
        LazyColumn {
            items(listaReceitas) { receita ->
                Text(
                    text = "${receita.titulo} - ${receita.descricao}",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row {
                    Button(onClick = {
                        receitaTemp = receita
                        mostrarCaixaDialogo = true
                    }) {
                        Text(text = "Excluir")
                    }

                    Button(onClick = {
                        modoEditar = true
                        receitaTemp = receita
                        titulo = receita.titulo
                        descricao = receita.descricao
                        textoBotao = "Atualizar"
                    }) {
                        Text(text = "Atualizar")
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
fun ExcluirReceita(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirmar exclusão") },
        text = { Text(text = "Tem certeza que deseja excluir esta receita?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Sim, excluir")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Não, cancelar")
            }
        }
    )
}