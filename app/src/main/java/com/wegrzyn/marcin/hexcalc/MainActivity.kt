package com.wegrzyn.marcin.hexcalc

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface( modifier = Modifier.fillMaxSize() ) {
                    MyConstr()
                }
            }
        }
    }
}

@Composable
fun MyConstr(){
    BoxWithConstraints {
        if (minWidth<600.dp){
            PortraitView()
        }else{
            LandscapeView()
        }
    }
}

@Composable
private fun PortraitView() {
    Column() {
        Display(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
        )
        Keyboard()
    }
}

@Composable
private fun LandscapeView(){
    Row() {
        Display(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.5f)
            .padding(end = 8.dp)
            ,shape = RoundedCornerShape(topEnd = 40.dp, bottomEnd = 40.dp))
        Keyboard()
    }
}

@Composable
fun Keyboard(model: MainViewModel = viewModel()) {

    val typeSelected by model.typeSelected.observeAsState()

//    Keyboard Buttons
    val buttonsList = arrayListOf(
        "D","E","F","/",
        "A","B","C","x",
        "7","8","9","-",
        "4","5","6","+",
        "1","2","3"," ",
        " ","0","<-","=" )

        MyGrid(Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)){
            buttonsList.forEach{
                FilledTonalButton(enabled = enableButton(bName = it, typeNumber = typeSelected?:TypeNumber.DEC)
                    , onClick = {
                    Repo.charDecoder(it) }, modifier = Modifier
                        .fillMaxWidth(1 / 4f)
                        .fillMaxHeight(1 / 6f)
                        .padding(4.dp)
                        .clip(CircleShape)) {
                    Text(text = it)
                }
            }

        }
    }
fun enableButton(bName: String, typeNumber: TypeNumber):Boolean {
    val hexLetterScope = 'A'..'F'
    val binLetterScope = '2'..'9'
    val name = bName.get(0)
    when(typeNumber){
        TypeNumber.DEC-> return !(name in hexLetterScope)
        TypeNumber.BIN-> return !(name in binLetterScope || name in hexLetterScope)
        else -> return true
    }
}

@Composable
fun MyGrid(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    Layout(modifier = modifier, content = content) { measurables, constraints ->

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)

        }
        layout(constraints.maxWidth, constraints.maxHeight){

            var yPosition = 0
            var xPosition = 0
            val numColumn = 4
            val numRow = 6
            val elemWidth = constraints.maxWidth/numColumn
            val elemHeight = constraints.maxHeight/2/numRow
            var number = 0


            placeables.forEach{placeable ->
                placeable.placeRelative(x=xPosition, y= yPosition)
                number++
                if(number%numColumn==0) {
                    number = 0
//                    yPosition += elemHeight
                    yPosition += placeable.height
                    xPosition = 0
                } else xPosition += elemWidth

            }
            Log.d(Repo.TAG, "placeble.height: ${placeables[5].height}" +
                    " width: ${placeables[5].width}" +
                    " constMaxHeight: ${constraints.maxHeight}" +
                    " width: ${constraints.maxWidth}")

        }
    }
}

@Composable
fun Display(model: MainViewModel = viewModel(), modifier: Modifier, shape: Shape){

    val hexText by model.hexString.observeAsState()
    val decText by model.decString.observeAsState()
    val binText by model.binString.observeAsState()
    val typeNum by model.typeSelected.observeAsState()

    val defColor = MaterialTheme.colorScheme.primary
    val selColor = MaterialTheme.colorScheme.onPrimaryContainer

    Card(modifier = modifier, shape = shape) {
        Column(horizontalAlignment = Alignment.End, modifier = Modifier
            .background(color = MaterialTheme.colorScheme.inverseOnSurface) ){
//            HEX
            Text(color = if (typeNum == TypeNumber.HEX) selColor else defColor
                , fontSize = 36.sp
                , modifier = Modifier
                    .padding(8.dp)
                , text = hexText.toString() )
//            DEC
            Text(color = if (typeNum == TypeNumber.DEC) selColor else defColor
                ,fontSize = 28.sp, modifier = Modifier
                .padding(8.dp)
                , text = decText.toString() )
//            BIN
            Text(color = if (typeNum == TypeNumber.BIN) selColor else defColor
                ,modifier = Modifier
                .padding(8.dp)
                , text = binText.toString())

        }
        RadioSw()
    }
}

@Composable
fun RadioSw( model: MainViewModel = viewModel() ){

    val typeSelected by model.typeSelected.observeAsState()

    fun set(s: TypeNumber){
        Repo.typeSelected.postValue(s)
    }
    Row(verticalAlignment = Alignment.Bottom
        , horizontalArrangement = Arrangement.Center
        , modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()) {
        val selColor = ButtonDefaults.filledTonalButtonColors(containerColor =MaterialTheme.colorScheme.onPrimary )
        val defColor = ButtonDefaults.filledTonalButtonColors()

        FilledTonalButton(colors = if(typeSelected==TypeNumber.HEX) selColor else defColor,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 0.dp
            , bottomStart = 20.dp, bottomEnd = 0.dp)
            , onClick = { set(TypeNumber.HEX)  }) {
            Row() {
                Text(text = "HEX")
            }
        }
        FilledTonalButton(colors = if(typeSelected==TypeNumber.DEC) selColor else defColor
            ,shape = RoundedCornerShape(0.dp), onClick = {
            set(TypeNumber.DEC)
        }) {
            Row() {
                Text(text = "DEC")  
            }
        }
        FilledTonalButton(colors = if(typeSelected==TypeNumber.BIN) selColor else defColor
            ,shape = RoundedCornerShape(topStart = 0.dp, topEnd = 20.dp
            , bottomStart = 0.dp, bottomEnd = 20.dp)
            , onClick = {
            set(TypeNumber.BIN)}) {
            Row() {
                Text(text = "BIN") 
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme() {
        PortraitView()
    }
}