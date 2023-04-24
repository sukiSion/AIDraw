package com.example.aidraw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aidraw.Bean.RequestBean
import com.example.aidraw.net.AppNetWork
import com.example.aidraw.net.AppService
import com.example.aidraw.util.ExUtil
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val requestBean = RequestBean(
            session_hash = ExUtil.getAndroidId(this)
        ).apply {
            setNavigation("(mutated hands and fingers:1.5 ), (long body :1.3), (mutation, poorly\n" +
                    "drawn :1.2) , black-white, bad anatomy, liquid body,liquid tongue, disfigured, malformed, mutated,anatomical nonsense, text font ui, error, malformed hands, long neck, blurred, lowers, lowres, bad anatomy, bad proportions, bad shadow,uncoordinated body, unnatural body, huge haunch, huge thighs, huge calf, bad hands,fused hand, missing hand, isappearing arms,disappearing thigh, disappearing calf, disappearing legs, fused ears, bad ears, poorly drawn ears, extra ears, liquid ears, heavy ears, missing ears, fused animal ears, bad animal ears, poorly drawn animal ears, extra animal ears, liquid animal ears, heavy animal ears,missing animal ears, text, ui, error, missing fingers,missing limb, fused fingers, one hand with more than 5 fingers, one hand with less than 5 fingers, one hand with more than 5 digit, one hand with less than 5 digit,extra digit, fewer digits, fused digit, missing digit, bad digit, liquid digit, colorful tongue, black tongue,cropped, watermark, username, blurry, JPEG artifacts, signature, 3D, 3D game, 3D game scene, 3D character,malformed feet, extra feet, bad feet, poorly drawn feet, fused feet, missing feet, extra shoes, bad shoes,fused shoes, more than two shoes, poorly drawn shoes, bad gloves, poorly drawn gloves, fused gloves,bad hairs,poorly drawn hairs, fused hairs, big muscles, ugly, bad face, fused face, poorly drawn face, cloned face, big face, long face, bad eyes, fused eyes poorly drawn eyes, extra eyes, malformed limbs, gross proportions. short arm,((missing arm), missing thighs, missing calf, missing legs, mutation, duplicate, morbid, mutilated, poorly drawn hands, more than 1 left hand, more than 1 right hand, deformed, (blurry), disfigured, missing legs, extra arms, extra thighs, more than 2 thighs, extra calf,fused calf, extra legs, bad knee, extra knee, more than 2 legs, bad tails, bad mouth, fused mouth, poorly drawn mouth, bad tongue, tongue within mouth, too long tongue, black tongue, big mouth, cracked mouth .bad mouth, dirty face, dirty teeth, dirty pantie, fused pantie, poorly drawn pantie, fused cloth, poorly drawn cloth, bad pantie, yellow teeth, thick lips, bad bad thigh gap, missing thigh gap, fused thigh gap, liquid thigh gap, poorly drawn thigh gap, poorly drawn anus, bad collarbone, fused collarbone, missing collarbone, liquid collarbone, strong girl, obesity, worst quality, low quality, normal quality, liquid tentacles,bad tentacles, poorly drawn tentacles, split tentacles,fused tentacles,  QR code, bar code,censored, safety panties, safety knickers, beard. ")
            setPosition("masterpiece, best quality, best quality,Amazing,beautiful detailed eyes,1girl,finely detail,Depth of field,extremely detailed CG unity 8k wallpaper, masterpiece, full body,(vtuber minato aqua),pink hair, blue streaked hair ")
        }

        runBlocking {
            AppNetWork.appService.getPhoto(requestBean)
        }
    }
}