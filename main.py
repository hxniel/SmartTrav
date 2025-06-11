from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List
import pickle
import pandas as pd
from sklearn.neighbors import NearestNeighbors

#Define request 
class TravelRequest(BaseModel):
    popularidad: str
    estacion: str
    rating: str
    precio: str
    clima: str
    idioma: str
    continente: str

class TravelResponse(BaseModel):
    recomendaciones: List[str]

# carga nuestro modelo serializado
with open("travel_model.pkl", "rb") as f:
    payload = pickle.load(f)

model      = payload["model"]       # arboldedecision
encoders   = payload["encoders"]    # labelencoder
df_destinos: pd.DataFrame = payload["data_df"]

# columnas codificadas
feature_cols = [
    "popularidad_enc",
    "estacion_enc",
    "rating_enc",
    "precio_enc",
    "clima_enc",
    "idioma_enc",
    "continente_enc"
]

# crearApi
app = FastAPI(title="Travel Recommender API")

@app.post("/predict", response_model=TravelResponse)
def predict(req: TravelRequest):
    data = req.dict()

    # construir vector de caracteristicas
    X = []
    for col in feature_cols:
        orig = col.replace("_enc", "")
        if orig in encoders:
            X.append(int(encoders[orig].transform([data[orig]])[0]))
        else:
            # si hay numeros
            X.append(data.get(orig))

    # usar el arbol de decision para predecir
    tipo_pred = model.predict([X])[0]

    
    cont_enc = encoders["continente"].transform([data["continente"]])[0]

    # filtrar destinos con predicción y continente
    sub_df = df_destinos[
        (df_destinos["tipo_enc"] == tipo_pred) &
        (df_destinos["continente_enc"] == cont_enc)
    ]

    # si no hay ninguno en ese cont
    if sub_df.empty:
        sub_df = df_destinos[df_destinos["tipo_enc"] == tipo_pred]

    # uso de knn para los otros resultados
    knn_sub = NearestNeighbors(n_neighbors=3)
    knn_sub.fit(sub_df[feature_cols].values)
    idxs = knn_sub.kneighbors([X], return_distance=False)[0]

    # recomendaciones mostradas
    recomendaciones = sub_df.iloc[idxs]["destino"].tolist()
    return TravelResponse(recomendaciones=recomendaciones)
