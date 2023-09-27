package com.upc.pry20231067.data.Souvenir

import com.upc.pry20231067.data.Souvenir.Souvenir

class SouvenirProvider {
    companion object{
        val listSouvenir = listOf<Souvenir>(
            Souvenir(   "Tumi",
                    "Cuchillo que utilizaban las culturas andinas precolombinas en sus ceremonias religiosas",
                        "https://cuzcoeats.com/wp-content/uploads/2015/12/tumi.jpg",
                    20.0,
                    30.0
                ),
            Souvenir(   "Inca",
                "Soberano del Imperio incaico",
                "https://elperuano.pe/fotografia/thumbnail/2019/09/07/000063714M.jpg",
                20.0,
                30.0
            ),
            Souvenir(   "Machu Picchu",
                "Ciudad antigua de los incas",
                "https://portal.andina.pe/EDPfotografia3/Thumbnail/2022/09/01/000895081W.jpg",
                20.0,
                30.0
            ),
        )
    }

}