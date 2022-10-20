package ru.tinkoff.tirecycler

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.tinkoff.tirecycler.coroutines.CoroutinesDemoActivity
import ru.tinkoff.tirecycler.databinding.ActivityMainBinding
import ru.tinkoff.tirecycler.rx.RxDemoActivity

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.buttonRx.setOnClickListener { startActivity(Intent(this, RxDemoActivity::class.java)) }
        binding.buttonCoroutines.setOnClickListener { startActivity(Intent(this, CoroutinesDemoActivity::class.java)) }
    }
}