# Proyecto de Seguimiento de Ubicación con Google Maps en Android

Este proyecto de Android, desarrollado con Kotlin, utiliza la librería de Google Maps y la ubicación del dispositivo para mostrar en tiempo real la posición del usuario y trazar un recorrido en el mapa. Además, se implementa la funcionalidad adicional de dibujar la ubicación solo cuando la precisión es inferior a 10 metros.

## Requisitos

- Solicitar la ubicación más precisa posible.
- Dibujar una nueva posición solo cuando la ubicación tiene una precisión inferior a 10 metros de error.
- Mantener el mapa centrado en la última ubicación recogida.

## Configuración del Proyecto

1. **Clonar el Repositorio:**
   ```bash
   git clone https://github.com/VicentePM/DrawLocation.git
   ```

2. **Configurar API Key de Google Maps:**
   - Obtener una clave de API de Google Cloud Platform.
   - Agregar la clave en el archivo `google_maps_api.xml` ubicado en `app/src/release/res/values/` y `app/src/debug/res/values/`.

3. **Ejecutar la Aplicación:**
   - Abrir el proyecto en Android Studio.
   - Conectar un dispositivo Android o utilizar un emulador.
   - Ejecutar la aplicación.

## Funcionalidades

1. **Ubicación en Tiempo Real:**
   - La aplicación muestra la ubicación del usuario en tiempo real sobre un mapa de Google.

2. **Dibujo de Recorrido:**
   - Se dibuja un recorrido en el mapa a medida que la ubicación del usuario cambia.

3. **Precisión de Ubicación:**
   - La aplicación filtra y dibuja solo las ubicaciones con una precisión inferior a 10 metros.

4. **Centrado Automático:**
   - El mapa se mantiene centrado en la última ubicación recogida, proporcionando una experiencia de usuario fluida.

## Dependencias Principales

- [Google Maps SDK](https://developers.google.com/maps/documentation/android-sdk/intro) - Librería de Google Maps para Android.
- [Google Play Services Location](https://developers.google.com/android/guides/setup) - Servicios de ubicación de Google Play.

## Contribuciones

¡Contribuciones son bienvenidas! Si encuentras algún problema o tienes sugerencias, por favor, abre un issue o envía un pull request.
