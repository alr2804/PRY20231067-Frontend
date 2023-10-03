package com.upc.pry20231067

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.PixelCopy
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.ar.core.Session
import com.google.ar.core.exceptions.UnavailableException
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.MaterialFactory
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
            createCube(anchorNode, Vector3(0.1f, 0.1f, 0.1f))
        }




        //take photo
        binding.captureButton.setOnClickListener{
            capturePhoto(arFragment.arSceneView)
        }

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