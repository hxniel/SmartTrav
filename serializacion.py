
import pandas as pd
import pickle
from sklearn.model_selection import train_test_split, GridSearchCV
from sklearn.preprocessing import LabelEncoder
from sklearn.tree import DecisionTreeClassifier, plot_tree
from sklearn.metrics import classification_report, accuracy_score
from sklearn.neighbors import NearestNeighbors
import matplotlib.pyplot as plt

df = pd.read_csv("datafinal.csv")

#codificación variables
label_encoders = {}
for col in ["tipo", "popularidad", "precio", "clima", "idioma", "continente", "rating", "estacion"]:
    le = LabelEncoder()
    df[col + "_enc"] = le.fit_transform(df[col])
    label_encoders[col] = le

#def variables
feature_cols = [
    "popularidad_enc", "estacion_enc", "rating_enc", "precio_enc",
    "clima_enc", "idioma_enc", "continente_enc"
]
X = df[feature_cols]
y = df["tipo_enc"]

#division train y test
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42
)

#optimizacion grid search
parametros = {
    "max_depth": [5, 10, 15, 20],
    "min_samples_split": [2, 4, 6],
    "min_samples_leaf": [1, 2, 4]
}

grid = GridSearchCV(
    estimator=DecisionTreeClassifier(random_state=42),
    param_grid=parametros,
    cv=5,
    scoring="accuracy",
    n_jobs=-1
)

grid.fit(X_train, y_train)

print("✅ Mejor configuración encontrada:")
print(grid.best_params_)

# Mejor modelo entrenado
clf = grid.best_estimator_

#ver arbol
plt.figure(figsize=(24, 12))
plot_tree(
    clf,
    feature_names=feature_cols,
    class_names=label_encoders["tipo"].classes_,
    filled=True,
    rounded=True,
    fontsize=10
)
plt.title("Árbol de Decisión - Modelo Optimizado")
plt.show()

#evaluacion
y_pred = clf.predict(X_test)
print("\nPrecisión (accuracy):", accuracy_score(y_test, y_pred))
print("\nReporte de clasificación:")
print(classification_report(
    y_test, y_pred,
    target_names=label_encoders["tipo"].classes_
))

#prueba de entrada
entrada_usuario = {
    "popularidad": "baja",
    "estacion": "primavera",
    "rating": "alto",
    "precio": "medio",
    "clima": "templado",
    "idioma": "inglés",
    "continente": "Europa"
}

entrada_codificada = {
    col + "_enc": label_encoders[col].transform([val])[0]
    for col, val in entrada_usuario.items()
}
entrada_df = pd.DataFrame([entrada_codificada])

#prediccion
tipo_pred = clf.predict(entrada_df)[0]
tipo_pred_nombre = label_encoders["tipo"].inverse_transform([tipo_pred])[0]
print(f"\n📌 Tipo predicho por el modelo: {tipo_pred_nombre}")

#filtrar destinos y recomendar
df_filtrado = df[
    (df["tipo_enc"] == tipo_pred) &
    (df["continente_enc"] == entrada_codificada["continente_enc"])
]
if df_filtrado.empty:
    print("⚠️ No se encontraron destinos en ese continente para el tipo predicho. Mostrando sin filtro de continente.")
    df_filtrado = df[df["tipo_enc"] == tipo_pred]

X_filtrado = df_filtrado[feature_cols]
nn = NearestNeighbors(n_neighbors=3)
nn.fit(X_filtrado)
_, indices = nn.kneighbors(entrada_df)

print("\n🎯 DESTINOS RECOMENDADOS:")
for i, idx in enumerate(indices[0]):
    destino = df_filtrado.iloc[idx]["destino"]
    print(f"{i+1}. {destino}")

#serializar el modelo
payload = {
    "model": clf,
    "encoders": label_encoders,
    "data_df": df
}
with open("travel_model.pkl", "wb") as f:
    pickle.dump(payload, f)
print("\n✅ Modelo serializado correctamente en travel_model.pkl")
