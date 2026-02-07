package com.example.glypher.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.glypher.R
import com.example.glypher.ui.theme.LocalGlyphColors
import com.example.glypher.utils.GetBuildInfo


@Composable
fun BuildInfoSection(info: GetBuildInfo){

        Row(Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically){

            buildInfo(info, Modifier.weight(1f))

            Spacer(modifier = Modifier.width(15.dp))

            Image(
                painter = painterResource(id = R.drawable.nothing_svg),
                contentDescription = "Phone Image",
                modifier = Modifier.size(180.dp).padding(10.dp),
                alignment = Alignment.Center

            )
        }
}

@Composable
fun buildInfo(buildInfo: GetBuildInfo, modifier: Modifier = Modifier){
    val color = LocalGlyphColors.current
    Column(modifier = modifier) {
        rowInfo("MANUFACTURE:", buildInfo.manufacture, color =  color.text)
        rowInfo("MODEL:", buildInfo.model, color =  color.text)
        rowInfo("SOC MODEL:", buildInfo.soc_model, color = color.text)
        if(buildInfo.isroot) rowInfo("ROOTED:", "ROOTED", color = color.text)
        else rowInfo("ROOTED:", "NOT ROOTED", color = color.warning)
    }
}

@Composable
fun rowInfo(label: String, value: String, color: Color){
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(text = value)
    }
}