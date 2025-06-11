
## Descripción

**Smart Travel Recommender** es un sistema que recomienda destinos de viaje en base a preferencias del usuario.  
- **Backend**: FastAPI + scikit-learn (Decision Tree + KNN)  
- **Mobile App**: Android Jetpack Compose + Retrofit + Coroutines

El flujo es:
1. El usuario responde secuencialmente a varias categorías (clima, tipo de viaje, presupuesto, etc.).  
2. La app envía esos datos al backend (`/predict`).  
3. FastAPI utiliza un árbol de decisión para clasificar el “tipo” y KNN para sugerir 3 destinos.  
4. La app muestra recomendaciones y permite reiniciar la búsqueda.


## Tecnologías

- **Backend**  
  - Python 3.8+  
  - FastAPI  
  - Uvicorn  
  - pandas, scikit-learn  
- **Mobile**  
  - Kotlin  
  - Jetpack Compose  
  - Retrofit + Gson  
  - Coroutines  
  - AndroidX Lifecycle & ViewModel-Compose


## Instalación y ejecución

### Backend

```bash
cd backend
# crear y activar virtualenv
python -m venv venv

después en entorno virtual ejecutar lo siguiente:
pip install --upgrade pip
pip install -r requirements.txt

# Ejecutar la API en localhost:8000
python -m uvicorn main:app --reload --host 0.0.0.0 --port 8000
