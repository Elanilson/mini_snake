package br.com.apkdoandroid.exemplo_mini_snake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import br.com.apkdoandroid.exemplo_mini_snake.databinding.ActivityMainBinding
import java.util.Random
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var meat : ImageView
    private lateinit var snake : ImageView
    private val snakeSegments : MutableList<ImageView> = mutableListOf()
    private var delayMillis = 30L // Atualize a posição da cobra a cada 100 milissegundos
    private var currentDirection = "right" // Comece a mover-se para a direita por padrão
    private var scorex = 0
    private  val handler = Handler()
    private  var snakeX : Float = 0f
    private  var snakeY : Float = 0f
    private val random = Random()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configIniciais()



    }

    private fun configIniciais(){



         meat = ImageView(this)
         snake = ImageView(this)

        snakeSegments.add(snake
        )
        binding.board.visibility = View.INVISIBLE
      //  binding.playagain.visibility = View.INVISIBLE
        binding.score.visibility = View.INVISIBLE
      //  binding.score2.visibility = View.INVISIBLE

        binding.newgame.setOnClickListener { novoJogo() }
    }

    private fun novoJogo(){

        binding. board.visibility = View.VISIBLE
        binding.newgame.visibility = View.INVISIBLE
        binding.resume.visibility = View.INVISIBLE
        binding.score.visibility = View.VISIBLE

        snake.setImageResource(R.drawable.baseline_lens_24) // cabeça_snake
        snake.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        Log.d("Jogo", "Cobra adicionada")
        binding.board.removeView(snake) // adicionei para ver se resolve o bug
        binding.board.addView(snake)
        snakeSegments.add(snake) // Adicione o novo segmento de cobra à lista

         snakeX = snake.x
         snakeY = snake.y


        meat.setImageResource(R.drawable.meat)
        meat.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        Log.d("Jogo", "Comida adicionada")
        binding.board.removeView(meat) // adicionei para ver se resolve o bug
        binding.board.addView(meat)

       // val random = Random() // crie um objeto aleatório
        val randomX =
            random.nextInt(801) - 400 // gerar uma coordenada x aleatória entre -400 e 400
        val randomY =
            random.nextInt(801) - 400 //gerar uma coordenada y aleatória entre -400 e 400


        meat.x = randomX.toFloat()
        meat.y = randomY.toFloat()

        atualizacaoDaTela()

        // Defina o botão onClickListeners para atualizar a variável currentDirection quando pressionado
        binding.up.setOnClickListener {
            currentDirection = "up"
        }
        binding.down.setOnClickListener {
            currentDirection = "down"
        }
        binding.left.setOnClickListener {
            currentDirection = "left"
        }
        binding.right.setOnClickListener {
            currentDirection = "right"
        }
        binding.pause.setOnClickListener {
            currentDirection = "pause"
            binding.board.visibility = View.INVISIBLE
            binding.newgame.visibility = View.VISIBLE
            binding.resume.visibility = View.VISIBLE

        }
        binding.resume.setOnClickListener {
            currentDirection = "right"
            binding.board.visibility = View.VISIBLE
            binding.newgame.visibility = View.INVISIBLE
            binding.resume.visibility = View.INVISIBLE

        }
        /* binding.playagain.setOnClickListener {

             recreate()
         }*/

    }

    private fun checkFoodCollision() {
        val distanceThreshold = 50


        val distance = sqrt((snake.x - meat.x).pow(2) + (snake.y - meat.y).pow(2))

        if (distance < distanceThreshold) { // Verifique se a distância entre a cabeça da cobra e a carne é menor que o limite

            val newSnake =
                ImageView(this) // Crie um novo ImageView para o segmento cobra adicional
            newSnake.setImageResource(R.drawable.baseline_lens_24)
            newSnake.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            binding.board.addView(newSnake)

            snakeSegments.add(newSnake) // Adicione o novo segmento de cobra à lista

            val randomX = random.nextInt(801) - -100
            val randomY = random.nextInt(801) - -100


            meat.x = randomX.toFloat()
            meat.y = randomY.toFloat()


            delayMillis-- // Reduza o valor do atraso em 1
            scorex++

            binding.score.text =    scorex.toString() //Atualizar visualização de texto com atraso


        }
    }

    private fun atualizacaoDaTela(){
        val runnable = object : Runnable {
            override fun run() {

                for (i in snakeSegments.size - 1 downTo 1) { // Atualize a posição de cada segmento da cobra, exceto a cabeça
                    snakeSegments[i].x = snakeSegments[i - 1].x
                    snakeSegments[i].y = snakeSegments[i - 1].y
                }


                when (currentDirection) {
                    "up" -> {
                        snakeY -= 10
                        if (snakeY < -490) { // Verifique se o ImageView sai do topo do quadro
                            Log.d("colisao", "Up ${snakeY} - ${snakeY < -490}")
                            snakeY = -490f
                            binding.border.setBackgroundColor(getResources().getColor(R.color.red))
                           // binding.playagain.visibility = View.VISIBLE
                            currentDirection = "pause"
                            binding.lilu.visibility = View.INVISIBLE

                            binding.score.text =  scorex.toString() //Atualizar visualização de texto com atraso
                            binding.score.visibility = View.VISIBLE
                           // score2.visibility = View.INVISIBLE
                            gameOver("Gamer Over","Sua pontuação é " + scorex.toString())



                        }
                        Log.d("currentDirection", "Up ${snakeY}")

                        snake.translationY = snakeY
                    }
                    "down" -> {
                        snakeY += 10
                        val maxY =
                            binding.board.height / 2 - snake.height + 30 // Calcule a coordenada y máxima
                        if (snakeY > maxY) { // Verifique se o ImageView sai da parte inferior do quadro
                            Log.d("colisao", "down  ${snakeY} - ${maxY}- ${snakeY > maxY}")
                            snakeY = maxY.toFloat()
                            binding.border.setBackgroundColor(getResources().getColor(R.color.red))
                            //binding.playagain.visibility = View.VISIBLE
                            currentDirection = "pause"
                            binding.lilu.visibility = View.INVISIBLE

                            binding.score.text =   scorex.toString() // Atualizar visualização de texto com atraso
                            binding.score.visibility = View.VISIBLE
                            //binding.score2.visibility = View.INVISIBLE
                            gameOver("Gamer Over","Sua pontuação é " + scorex.toString())

                        }
                        Log.d("currentDirection", "down ${snakeY}")
                        snake.translationY = snakeY
                    }
                    "left" -> {
                        snakeX -= 10
                        if (snakeX < -490) { // Verifique se o ImageView sai do topo do quadro
                            Log.d("colisao", "left ${snakeX} - ${snakeX < -490}")
                            snakeX = -490f
                            binding.border.setBackgroundColor(getResources().getColor(R.color.red))
                            //binding.playagain.visibility = View.VISIBLE
                            currentDirection = "pause"
                            binding.lilu.visibility = View.INVISIBLE
                            binding.score.text =  scorex.toString() // Atualizar visualização de texto com atraso
                            binding.score.visibility = View.VISIBLE
                           // binding.score2.visibility = View.INVISIBLE
                            gameOver("Gamer Over","Sua pontuação é " + scorex.toString())


                        }
                        Log.d("currentDirection", "left ${snakeX}")
                        snake.translationX = snakeX
                    }
                    "right" -> {
                        snakeX += 10
                        val maxX =
                            binding.board.height / 2 - snake.height + 30 // Calcule a coordenada y máxima
                        if (snakeX > maxX) { // Verifique se o ImageView sai da parte inferior do quadro
                            Log.d("colisao", "right ${snakeX} - ${maxX}- ${snakeX > maxX}")
                            snakeX = maxX.toFloat()
                            binding.border.setBackgroundColor(getResources().getColor(R.color.red))
                           // binding.playagain.visibility = View.VISIBLE
                            currentDirection = "pause"
                            binding.lilu.visibility = View.INVISIBLE

                            binding.score.text =   scorex.toString() // Atualizar visualização de texto com atraso
                            binding.score.visibility = View.VISIBLE
                           // binding.score2.visibility = View.INVISIBLE
                            gameOver("Gamer Over","Sua pontuação é " + scorex.toString())


                        }
                        Log.d("currentDirection", "right ${snakeX}")
                        snake.translationX = snakeX
                    }

                    "pause" -> {
                        snakeX += 0
                        snake.translationX = snakeX
                        Log.d("currentDirection", "pause ${snakeX}")

                    }
                }

                checkFoodCollision()
                handler.postDelayed(this, delayMillis)
            }
        }

        handler.postDelayed(runnable, delayMillis)
    }

    fun gameOver( titulo: String, mensagem: String) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle(titulo)
        builder.setMessage(mensagem)
        builder.setPositiveButton("FIM (HOME)") { dialog, _ ->
            recreate()
            dialog.dismiss() // Fecha o diálogo
        }
        val dialog = builder.create()
        dialog.show()
    }
}