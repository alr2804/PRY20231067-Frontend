package com.upc.pry20231067

import android.content.ContentValues
import android.graphics.Bitmap
import android.icu.text.CaseMap.Title
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.PixelCopy
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import com.google.ar.core.Session
import com.google.ar.core.exceptions.UnavailableException
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.upc.pry20231067.common.Utils.showToast
import com.upc.pry20231067.databinding.ActivityArBinding
import com.upc.pry20231067.helpers.ARCoreSessionLifecycleHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ArActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArBinding


    //my variables
    private lateinit var arFragment: ArFragment
    lateinit var arCoreSessionHelper: ARCoreSessionLifecycleHelper
    private var arSession: Session? = null

    // Reference to the currently selected node
    private var selectedNode: TransformableNode? = null
    private lateinit var deleteButton: Button

    data class Item(val id: Int, val name: String, val description: String)

    private val itemsArray = arrayOf(
        Item(R.raw.elemento1, "Botellas Escultóricas Gemelas", "Descripción: Cerámica conectada en forma de aves.\n" +
                "Significado: Simboliza lo divino; posiblemente usadas en ceremonias.\n" +
                "Era: 100-700 d.C."),
        Item(R.raw.elemento2, "Falsas Cabezas de Madera", "Descripción: Tallas de madera para rituales funerarios.\n" +
                "Significado: Representan almas o compañeros para el más allá.\n" +
                "Era: 100 a.C.-800 d.C."),
        Item(R.raw.elemento3, "Cerámica en Forma de Venado", "Descripción: Recipiente cerámico con forma realista de venado.\n" +
                "Significado: Símbolo de fertilidad y abundancia.\n" +
                "Era: 100-800 d.C."),
        Item(R.raw.elemento4, "Escultura de Madera Chimú", "Descripción: Figura tallada en madera de significación religiosa y social.\n" +
                "Significado: Representa deidades o autoridades.\n" +
                "Era: 1100-1470 d.C."),
        Item(R.raw.elemento5, "Cántaro Tricolor Cara Gollete", "Descripción: Vasija tricolor con cara en el gollete.\n" +
                "Significado: Utilizado para chicha en rituales/ofrendas.\n" +
                "Era: 1400-1532 d.C."),
    )

    private var currentIndex = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //arView
        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment
        arCoreSessionHelper = ARCoreSessionLifecycleHelper(this)



        try {
            arSession = Session(this)
        } catch (e: UnavailableException) {
            // Handle exceptions: ARCore not supported or some other issue.
            showToast(this, e.toString())
        }




        deleteButton = binding.deleteButton
        deleteButton.visibility = View.GONE

        deleteButton.setOnClickListener {
            selectedNode?.let { node ->
                node.setParent(null)  // This removes the TransformableNode from its parent AnchorNode
                deleteButton.visibility = View.GONE
                selectedNode = null   // Nullify the reference
            }
        }


        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            showToast(this,"tap")
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)
//            createCube(anchorNode, Vector3(0.1f, 0.1f, 0.1f))
//            createModel(anchorNode, R.raw.untitled )
//            createModelWithText(anchorNode, R.raw.untitled)


            createModelWithText(anchorNode, itemsArray[currentIndex].id, itemsArray[currentIndex].name)
            showDialog(itemsArray[currentIndex].description)
            currentIndex = (currentIndex + 1) % itemsArray.size




        }





        //take photo
        binding.captureButton.setOnClickListener{
            capturePhoto(arFragment.arSceneView)
        }

    }

    private fun showDialog(text:String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(text)
            .setPositiveButton("Aceptar") { dialog, id ->
                // Acción para el botón Aceptar
            }


        val dialog = builder.create()
        dialog.show()
    }

    private fun createCube(anchorNode: AnchorNode, cubeSize: Vector3) {
        MaterialFactory.makeOpaqueWithColor(this, Color(0.0f, 0.5f, 0.5f))
            .thenAccept { material ->
                val cubeRenderable = ShapeFactory.makeCube(
                    cubeSize,   // Size of the cube
                    Vector3(0f, cubeSize.y / 2, 0f),  // Center of the cube
                    material
                )

                val cubeNode = TransformableNode(arFragment.transformationSystem)
                cubeNode.renderable = cubeRenderable
                cubeNode.setParent(anchorNode)

                // Programmatically create a TextView
                val textView = TextView(this)
                textView.text = "Your Text"
                textView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                textView.setTextColor(android.graphics.Color.rgb(0, 0, 0))
                textView.setBackgroundColor(android.graphics.Color.argb(50, 255, 255, 255))


                // Create the text renderable
                ViewRenderable.builder()
                    .setView(this, textView)
                    .build()
                    .thenAccept { viewRenderable ->
                        val textNode = Node()
                        textNode.renderable = viewRenderable
                        textNode.setParent(cubeNode)

                        // Adjust the position of the text so that it appears on top of the cube
                        textNode.localPosition = Vector3(0f, cubeSize.y + 0.05f + 0.01f, 0f)

                    }


                cubeNode.setOnTapListener { _, _ ->
                    if (cubeNode == arFragment.transformationSystem.selectedNode) {
                        selectedNode = cubeNode
                        deleteButton.visibility = View.VISIBLE
                    } else {
                        deleteButton.visibility = View.GONE
                        selectedNode = null
                    }
                }


                cubeNode.select()


            }
    }

    private fun createModelWithText(anchorNode: AnchorNode, modelResourceId: Int, title: String) {
        ModelRenderable.builder()
            .setSource(this, modelResourceId)
            .build()
            .thenAccept { modelRenderable ->
                val modelNode = TransformableNode(arFragment.transformationSystem)
                modelNode.renderable = modelRenderable
                modelNode.setParent(anchorNode)

                // Programmatically create a TextView
                val textView = TextView(this).apply {
                    text = title
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setTextColor(android.graphics.Color.rgb(0, 0, 0))
                    setBackgroundColor(android.graphics.Color.argb(50, 255, 255, 255))
                }

                // Create the text renderable
                ViewRenderable.builder()
                    .setView(this, textView)
                    .build()
                    .thenAccept { viewRenderable ->
                        Node().apply {
                            renderable = viewRenderable
                            setParent(modelNode)
                            // Position the text slightly above the model (adjust as needed)
                            localPosition = Vector3(0f, 0.15f, 0.15f)
                        }
                    }

                modelNode.setOnTapListener { _, _ ->
                    if (modelNode == arFragment.transformationSystem.selectedNode) {
                        selectedNode = modelNode
                        deleteButton.visibility = View.VISIBLE
                    } else {
                        deleteButton.visibility = View.GONE
                        selectedNode = null
                    }
                }

                modelNode.select()
            }
            .exceptionally {
                Toast.makeText(this, "Error loading the model", Toast.LENGTH_SHORT).show()
                null
            }
    }




//    private fun createModel(anchorNode: AnchorNode, modelResourceId: Int) {
//        ModelRenderable.builder()
//            .setSource(this, modelResourceId)
//            .build()
//            .thenAccept { renderable ->
//                val node = TransformableNode(arFragment.transformationSystem)
//                node.renderable = renderable
//                node.setParent(anchorNode)
//
//            }
//            .exceptionally {
//                Toast.makeText(this, "Error loading the model", Toast.LENGTH_LONG).show()
//                null
//            }
//    }

//    private fun createModel(anchorNode: AnchorNode, modelResourceId: Int) {
//        ModelRenderable.builder()
//            .setSource(this, modelResourceId)
//            .build()
//            .thenAccept { renderable ->
//                val modelNode = TransformableNode(arFragment.transformationSystem)
//                modelNode.renderable = renderable
//                modelNode.setParent(anchorNode)
//
//                // Create and set up the TextView
//                val textView = TextView(this).apply {
//                    text = "Your Text Here"
//                    textSize = 16f
//                    setBackgroundColor(ContextCompat.getColor(this@ArActivity, android.R.color.transparent))
//                }
//
//                // Convert the TextView to a ViewRenderable
//                ViewRenderable.builder()
//                    .setView(this, textView)
//                    .build()
//                    .thenAccept { viewRenderable ->
//                        Node().apply {
//                            renderable = viewRenderable
//                            setParent(modelNode)
//                            localPosition = Vector3(0f, 0.1f, 0f)  // Adjust as needed
//                        }
//                    }
//            }
//            .exceptionally {
//                Toast.makeText(this, "Error loading the model", Toast.LENGTH_SHORT).show()
//                null
//            }
//    }

    private fun capturePhoto(sceneView: ArSceneView) {
        val bitmap = Bitmap.createBitmap(sceneView.width, sceneView.height, Bitmap.Config.ARGB_8888)

        PixelCopy.request(sceneView, bitmap, { copyResult ->
            if (copyResult == PixelCopy.SUCCESS) {
                saveBitmapToMediaStore(bitmap)
                Toast.makeText(this, "Photo captured!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to capture photo!", Toast.LENGTH_SHORT).show()
            }
        }, Handler(Looper.getMainLooper()))
    }

    private fun saveBitmapToMediaStore(bitmap: Bitmap) {
        val filename = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date()) + ".png"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/YourAppFolder")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            contentResolver.openOutputStream(it)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                contentResolver.update(uri, contentValues, null, null)
            }
        }
    }



}