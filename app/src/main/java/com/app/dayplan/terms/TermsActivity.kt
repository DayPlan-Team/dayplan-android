package com.app.dayplan.terms

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.dayplan.api.auth.ApiAuthClient
import com.app.dayplan.home.HomeActivity
import com.app.dayplan.ui.theme.DayplanTheme
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

class TermsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DayplanTheme {
                TermsScreen()
            }
        }
    }

    @Preview
    @Composable
    fun TermsScreen() {
        val coroutineScope = rememberCoroutineScope()
        var termsList by remember { mutableStateOf<List<Terms>?>(null) }

        LaunchedEffect(key1 = Unit) { // key1은 API 호출이 한 번만 이루어지게 하는 역할
            coroutineScope.launch {
                try {

                    val response = ApiAuthClient.termsService.getTerms()
                    if (response.isSuccessful) {
                        response.body().let {
                            termsList = it!!.terms
                        }
                    }
                } catch (e: Exception) {
                    Log.e("API Error = ", e.toString())
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                termsList == null -> {
                    Log.i("terms List = ", "error")
                    CircularProgressIndicator()
                }

                termsList!!.isEmpty() -> {
                    Text(text = "에러가 발생했어요. 잠시 후에 요청 부탁드려요")
                }

                else -> {
                    TermsListUI(termsList = termsList!!)
                }
            }
        }
    }

    @Composable
    fun TermsListUI(termsList: List<Terms> = emptyList()) {
        val coroutineScope = rememberCoroutineScope()
        var allSelected by remember { mutableStateOf(false) }
        var termsAgreements by remember { mutableStateOf(mutableMapOf<Long, Boolean>()) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val termCheckStates =
                remember { mutableStateListOf<Boolean>().apply { addAll(List(termsList.size) { false }) } }

            fun onTermCheckedChanged(terms: Terms, index: Int, isChecked: Boolean) {
                termCheckStates[index] = isChecked
                termsAgreements[terms.termsId.toLong()] = isChecked
                Log.i("Checkbox Change", "TermId: ${terms.termsId}, IsChecked: $isChecked")
            }

            // 전체 약관이 체크되었는지 확인하는 함수
            fun areAllTermsChecked(states: List<Boolean>): Boolean {
                return states.all { it }
            }

            // "전체 선택" 체크박스의 변경을 처리하는 함수
            fun onAllTermsCheckedChanged(termsList: List<Terms>, isChecked: Boolean, states: MutableList<Boolean>) {
                val modifiedStates = states.toMutableList()
                modifiedStates.forEachIndexed { index, _ ->
                    modifiedStates[index] = isChecked
                }
                termsList.forEach {
                    termsAgreements[it.termsId.toLong()] = isChecked
                    Log.i("All Checkbox Change", "TermId: ${it.termsId}, IsChecked: $isChecked")
                }
                termCheckStates.clear()
                termCheckStates.addAll(modifiedStates)
            }

            termsList.forEachIndexed { index, term ->
                val isChecked = termCheckStates[index]
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = term.content + if (term.mandatory) " (필수)" else " (선택)")
                    Checkbox(checked = isChecked, onCheckedChange = { newValue ->
                        // 여기서 termCheckStates를 직접 수정하지 않고, 기능을 분리해 별도의 함수를 사용합니다.
                        onTermCheckedChanged(term, index, newValue)
                        // 전체 선택 상태를 체크하는 로직도 별도의 함수로 분리할 수 있습니다.
                        allSelected = areAllTermsChecked(termCheckStates)
                    })
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "전체 선택")
                Checkbox(checked = allSelected, onCheckedChange = { isChecked ->
                    onAllTermsCheckedChanged(termsList, isChecked, termCheckStates)
                })
            }

            LaunchedEffect(key1 = Unit) { // key1은 API 호출이 한 번만 이루어지게 하는 역할
                coroutineScope.launch {
                    try {

                    } catch (e: Exception) {
                        Log.e("API Error = ", e.toString())
                    }
                }
            }


            Button(
                onClick = {

                    coroutineScope.launch {
                        Log.i("Coroutine = ", "is Start")
                        Log.i("termsAgreement = ", termsAgreements.toString())
                        try {
                            upsertTermsAgreement(
                                TermsAgreements(termsAgreements.map {
                                    TermsAgreement(it.key, it.value)
                                })
                            )
                        } catch (e: Exception) {
                            Log.e("API Error = ", e.toString())
                        }
                    }

                }, modifier = Modifier.align(Alignment.End)
            ) {
                Text("동의하기")
            }
        }
    }

    private suspend fun upsertTermsAgreement(termsAgreements: TermsAgreements) {
        Log.i("termsAgreements = ", termsAgreements.termsAgreements.toString())
        val response = ApiAuthClient.termsService.upsetTermsAgreements(termsAgreements)

        if (response.isSuccessful) {
            Log.i("Response is Success", "Response is Success")
            val intent = Intent(this@TermsActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.i("error", response.code().toString())
        }
    }
}