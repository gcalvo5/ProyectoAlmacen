package com.example.proyectoalmacen.viewmodel.UseCases

import com.example.proyectoalmacen.model.States.UiState
import com.example.proyectoalmacen.viewmodel.DataManagers.DataManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UseCaseGetExpedicionesNumBultosTotales @Inject constructor(private val dataManager: DataManager){
    fun execute(codPlaza:Int = 0): Flow<UiState<List<Int>>> = flow{
        //pos 0 = numTotales, pos 1 = numConfirmados, pos 2 = numRepasados, pos 3 = numCargados
        var listaCantidades = mutableListOf<Int>(0,0,0,0)
        dataManager.expedicionesListState.collect{ state ->
            when(state){
                is UiState.Success -> {
                    state.data.forEach { expedicion ->
                        if (codPlaza > 0){
                            if (expedicion.codPlaza == codPlaza){
                                listaCantidades[0] = listaCantidades[0] + expedicion.numBultos
                                listaCantidades[1] = listaCantidades[1] + expedicion.numBultosConfirmados
                                listaCantidades[2] = listaCantidades[2] + expedicion.numBultosRepasados
                                listaCantidades[3] = listaCantidades[3] + expedicion.numbultosCargados
                            }
                        }else {
                            listaCantidades[0] = listaCantidades[0] + expedicion.numBultos
                            listaCantidades[1] =
                                listaCantidades[1] + expedicion.numBultosConfirmados
                            listaCantidades[2] = listaCantidades[2] + expedicion.numBultosRepasados
                            listaCantidades[3] = listaCantidades[3] + expedicion.numbultosCargados
                        }
                    }
                    emit(UiState.Success(listaCantidades))
                }
                is UiState.Error -> {
                    emit(UiState.Error(state.message))
                }
                is UiState.Loading -> {
                    emit(UiState.Loading)
                }
            }

        }
    }
}