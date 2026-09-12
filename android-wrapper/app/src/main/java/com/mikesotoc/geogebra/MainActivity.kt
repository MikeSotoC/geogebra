package com.mikesotoc.geogebra
import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
class MainActivity:Activity(){override fun onCreate(b:Bundle?){super.onCreate(b);val root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;gravity=Gravity.CENTER};root.addView(TextView(this).apply{text="GeoGebra";textSize=28f});root.addView(TextView(this).apply{text="Android nativo · offline";textSize=16f});setContentView(root)}}