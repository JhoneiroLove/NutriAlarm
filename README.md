# 🍓 NutriAlarm

**Alimentación saludable contra la anemia**

NutriAlarm es una aplicación móvil diseñada para combatir la anemia a través de una alimentación inteligente y personalizada. La app ayuda a los usuarios a mantener una dieta rica en hierro mediante recordatorios personalizados, seguimiento nutricional y recomendaciones adaptadas a su nivel de riesgo de anemia.

---

## 📱 Características Principales

### 🎯 Gestión Personalizada de Comidas
- **Menús Personalizados:** Selección de comidas específicas para cada momento del día
- **6 Tipos de Comidas:** Desayuno, refrigerio escolar, almuerzo, merienda de tarde, cena y snack opcional
- **Horarios Flexibles:** Configuración de recordatorios adaptados a tu rutina

### 🔔 Sistema de Alarmas Inteligentes
- **Recordatorios Adaptativos:** Notificaciones personalizadas según el tipo de comida
- **Programación Semanal:** Configuración de alarmas para diferentes días
- **Mensajes Contextuales:** Recordatorios específicos para cada comida seleccionada

### 📊 Seguimiento Nutricional Avanzado
- **Monitoreo de Hierro:** Seguimiento diario del consumo de hierro
- **Balance Calórico:** Control de calorías consumidas vs. objetivo diario
- **Nutrientes Complementarios:** Tracking de vitamina C y folato para mejor absorción
- **Progreso Visual:** Indicadores gráficos del progreso nutricional

### 🩺 Evaluación de Salud Personalizada
- **Análisis de IMC:** Cálculo automático del índice de masa corporal
- **Evaluación de Riesgo:** Clasificación del riesgo de anemia (bajo, medio, alto)
- **Recomendaciones Personalizadas:** Consejos específicos según tu perfil de salud
- **Seguimiento de Tendencias:** Historial de progreso nutricional

### 🍽️ Base de Datos Expandida de Alimentos
- **+80 Recetas Antianoemia:** Extenso catálogo de comidas ricas en hierro
- **Información Nutricional Completa:** Datos detallados de hierro, calorías, vitaminas
- **Recetas Detalladas:** Ingredientes, preparación y beneficios nutricionales
- **Categorización Inteligente:** Organización por tipo de comida y valor nutricional

### 🎨 Experiencia de Usuario Moderna
- **Material Design 3:** Interfaz moderna y accesible
- **Animaciones Fluidas:** Transiciones suaves y naturales
- **Tema Adaptativo:** Soporte para modo claro y oscuro
- **Responsive Design:** Optimizado para diferentes tamaños de pantalla

### 📱 Monetización Integrada
- **AdMob Integration:** Anuncios banner, intersticiales y recompensados
- **Bonus de Hierro:** Sistema de recompensas por visualización de anuncios
- **Experiencia No Intrusiva:** Anuncios integrados de forma natural

---

## 🏗️ Arquitectura Técnica

### Patrón de Arquitectura

```

📦 Arquitectura MVVM + Clean Architecture
├── 🎨 Presentation Layer (UI/ViewModels)
├── 📋 Domain Layer (Use Cases/Entities)
└── 💾 Data Layer (Repositories/Data Sources)

```

### Stack Tecnológico

| Categoría                 | Tecnología                    | Propósito                                   |
|---------------------------|-------------------------------|---------------------------------------------|
| UI Framework              | Jetpack Compose               | Interfaz de usuario declarativa             |
| Arquitectura              | MVVM + Clean Architecture     | Separación de responsabilidades             |
| Inyección de Dependencias | Dagger Hilt                   | Gestión de dependencias                     |
| Base de Datos Local       | Room Database                 | Almacenamiento local SQLite                 |
| Backend                   | Firebase (Auth + Firestore)   | Autenticación y sincronización              |
| Concurrencia              | Kotlin Coroutines + Flow      | Programación asíncrona                      |
| Navegación                | Navigation Component          | Navegación entre pantallas                  |
| Background Work           | WorkManager                   | Tareas en segundo plano                     |
| Notificaciones            | AlarmManager + Notifications  | Sistema de recordatorios                    |
| Monetización              | Google AdMob                  | Anuncios y monetización                     |
| Networking                | Retrofit + OkHttp             | Comunicación con APIs                       |

---

## Estructura del Proyecto

```

📂 com.upao.nutrialarm/
├── 📂 data/
│   ├── 📂 local/
│   │   ├── 📂 database/
│   │   │   ├── 📂 dao/           # Data Access Objects
│   │   │   ├── 📂 entities/      # Entidades de Room
│   │   │   └── 📂 relations/     # Relaciones entre entidades
│   │   └── 📂 preferences/       # SharedPreferences management
│   ├── 📂 remote/
│   │   ├── 📂 firebase/         # Firebase Auth & Firestore
│   │   └── 📂 admob/            # Integración AdMob
│   └── 📂 repository/           # Implementaciones de repositorios
├── 📂 domain/
│   ├── 📂 model/                # Modelos de dominio
│   ├── 📂 repository/           # Interfaces de repositorios
│   └── 📂 usecase/              # Casos de uso de negocio
├── 📂 presentation/
│   ├── 📂 auth/                 # Pantallas de autenticación
│   ├── 📂 home/                 # Pantalla principal
│   ├── 📂 profile/              # Gestión de perfil
│   ├── 📂 diet/                 # Dietas y planes alimentarios
│   ├── 📂 meal/                 # Selección y recetas de comidas
│   ├── 📂 component/            # Componentes reutilizables
│   └── 📂 admob/                # Helpers de AdMob
├── 📂 util/                     # Utilidades y helpers
└── 📂 ui/theme/                 # Tema y colores de la app

````

---

## 🚀 Instalación y Configuración

### Prerrequisitos

- Android Studio Hedgehog (2023.1.1) o superior  
- JDK 8 o superior  
- Android SDK API 23+ (Android 6.0)  
- Cuenta de Firebase para configuración del backend  
- Cuenta de AdMob para monetización  

### Configuración del Proyecto

**Clonar el repositorio**

```bash
git clone https://github.com/JhoneiroLove/nutrialarm.git
cd nutrialarm
````

**Configurar Firebase**

```bash
# Descargar google-services.json desde Firebase Console
# Colocar en: app/google-services.json
```

**Configurar AdMob**

```xml
<!-- En AndroidManifest.xml, reemplazar con tu Application ID real -->
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX"/>
```

**Compilar y ejecutar**

```bash
./gradlew assembleDebug
# O usar Android Studio: Run > Run 'app'
```

#### Configuración de Firebase

1. Crear proyecto en Firebase Console
2. Habilitar Authentication (Email/Password)
3. Configurar Firestore Database
4. Descargar google-services.json
5. Configurar reglas de seguridad de Firestore

#### Configuración de AdMob

1. Crear cuenta en AdMob
2. Crear aplicación y obtener Application ID
3. Crear unidades publicitarias:

   * Banner Ad Unit
   * Interstitial Ad Unit
   * Rewarded Ad Unit
4. Actualizar IDs en AdMobService.kt

---

## 💾 Base de Datos

### Esquema de Base de Datos Local (Room)

```sql
-- Tabla de usuarios
CREATE TABLE users (
    id TEXT PRIMARY KEY,
    email TEXT NOT NULL,
    name TEXT NOT NULL,
    age INTEGER NOT NULL,
    weight REAL NOT NULL,
    height REAL NOT NULL,
    activityLevel TEXT NOT NULL,
    anemiaRisk TEXT NOT NULL,
    createdAt INTEGER NOT NULL
);

-- Tabla de comidas
CREATE TABLE meals (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    ingredients TEXT NOT NULL, -- JSON array
    mealType TEXT NOT NULL,
    ironContent REAL NOT NULL,
    calories REAL NOT NULL,
    preparationTime INTEGER NOT NULL,
    imageUrl TEXT NOT NULL,
    vitaminC REAL NOT NULL,
    folate REAL NOT NULL,
    isPreloaded INTEGER DEFAULT 1
);

-- Tabla de dietas
CREATE TABLE diets (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    anemiaRiskLevel TEXT NOT NULL,
    ironContent REAL NOT NULL,
    calories REAL NOT NULL,
    isPreloaded INTEGER DEFAULT 1
);

-- Tabla de preferencias de usuario
CREATE TABLE user_meal_preferences (
    id TEXT PRIMARY KEY,
    userId TEXT NOT NULL,
    mealType TEXT NOT NULL,
    selectedMealId TEXT NOT NULL,
    timeSlot TEXT NOT NULL,
    isActive INTEGER DEFAULT 1,
    reminderEnabled INTEGER DEFAULT 1,
    createdAt INTEGER NOT NULL,
    updatedAt INTEGER NOT NULL
);

-- Tabla de consumo de comidas
CREATE TABLE meal_consumption (
    id TEXT PRIMARY KEY,
    userId TEXT NOT NULL,
    mealId TEXT NOT NULL,
    mealType TEXT NOT NULL,
    consumedAt INTEGER NOT NULL,
    date TEXT NOT NULL,
    ironContent REAL NOT NULL,
    calories REAL NOT NULL,
    vitaminC REAL NOT NULL,
    folate REAL NOT NULL
);

-- Tabla de alarmas
CREATE TABLE alarms (
    id TEXT PRIMARY KEY,
    userId TEXT NOT NULL,
    mealType TEXT NOT NULL,
    time TEXT NOT NULL,
    isEnabled INTEGER DEFAULT 1,
    days TEXT NOT NULL, -- JSON array
    reminderMessage TEXT NOT NULL,
    createdAt INTEGER NOT NULL
);
```

---

## 🎨 Diseño y UI/UX

### Paleta de Colores

```kotlin
// Colores principales
val NutriBlue = Color(0xFF2563EB)      // Azul principal
val NutriGreen = Color(0xFF16A34A)     // Verde éxito
val NutriOrange = Color(0xFFEA580C)    // Naranja energia
val NutriRed = Color(0xFFDC2626)       // Rojo alerta
val IronRed = Color(0xFFB91C1C)        // Rojo hierro
val NutriGrayDark = Color(0xFF374151)  // Gris oscuro
val NutriGray = Color(0xFF6B7280)      // Gris medio
```

### Componentes de UI Personalizados

* **NutriButton:** Botones con diferentes variantes (Primary, Secondary, Success, Warning, Danger)
* **MealCard:** Tarjetas de comidas con información nutricional
* **MealSelectorDialog:** Selector de comidas específicas
* **BannerAdView:** Integración de anuncios banner
* **LoadingDialog:** Diálogos de carga animados

### Animaciones y Transiciones

* Animaciones de entrada escalonadas para listas
* Transiciones suaves entre pantallas
* Micro-animaciones en botones y tarjetas
* Indicadores de progreso animados

---

## 📊 Características Técnicas Avanzadas

### Sistema de Notificaciones

```kotlin
// Programación de alarmas exactas
AlarmManager + BroadcastReceiver para recordatorios precisos
NotificationCompat para notificaciones modernas
Soporte para Android 13+ (notificaciones en tiempo de ejecución)
```

### Persistencia y Sincronización

```kotlin
// Estrategia de datos offline-first
Room Database para almacenamiento local
Firebase Firestore para sincronización en la nube
Manejo de conflictos de datos automático
```

### Optimizaciones de Rendimiento

```kotlin
// Lazy loading de datos
Paginación en listas grandes
Caching inteligente de imágenes
Optimización de consultas de base de datos
```

### Monetización Inteligente

```kotlin
// Sistema de recompensas por anuncios
AdMob Rewarded Ads para bonus de hierro
Intersticiales en momentos naturales
Banner ads no intrusivos
```

---

## 🧪 Testing

### Estrategia de Testing

```bash
# Tests unitarios
./gradlew test

# Tests de instrumentación
./gradlew connectedAndroidTest

# Tests de UI con Compose
./gradlew testDebugUnitTest
```

### Cobertura de Tests

* ViewModels: 85%+
* Use Cases: 90%+
* Repositories: 80%+
* Componentes UI: 70%+

---

## 📈 Métricas y Analytics

### Eventos Tracked

* Registro de nuevos usuarios
* Configuración de preferencias alimentarias
* Consumo de comidas registrado
* Interacciones con anuncios
* Tiempo de uso de la aplicación

### KPIs Principales

* Retención de usuarios (D1, D7, D30)
* Frecuencia de uso de alarmas
* Progreso nutricional promedio
* Revenue per user (AdMob)

---

## 🚀 Roadmap y Futuras Funcionalidades

**Versión 2.0 (Q2 2025)**

* Integración con Apple Health / Google Fit
* Modo offline completo
* Exportación de reportes nutricionales
* Integración con wearables

**Versión 2.1 (Q3 2025)**

* Reconocimiento de alimentos por cámara
* Chatbot nutricional con IA
* Comunidad y social features
* Gamificación avanzada

**Versión 2.2 (Q4 2025)**

* Consultas con nutricionistas
* Planes de suscripción premium
* Análisis de sangre integrados
* Recetas con RA

---

## 🤝 Contribución

### Cómo Contribuir

1. Fork el proyecto
2. Crear una rama feature (`git checkout -b feature/AmazingFeature`)
3. Commit los cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

### Estándares de Código

* Seguir las convenciones de Kotlin
* Usar KDoc para documentación
* Tests unitarios para nuevas funcionalidades
* Seguir el patrón de arquitectura establecido

### Reportar Bugs

* Usar el template de issues
* Incluir pasos para reproducir
* Adjuntar logs relevantes
* Especificar versión de Android

---

## 📄 Licencia

Este proyecto está licenciado bajo la Licencia Apache 2.0. Ver el archivo LICENSE para más detalles.

---

## 👥 Equipo

### Desarrolladores

* **Jhoel Maqui** - Desarrollador Principal - [@JhoneiroLove](https://github.com/JhoneiroLove)

### Colaboradores

* Nutricionistas Consultores - Validación de contenido nutricional
* Diseñadores UX/UI - Experiencia de usuario
* Testers Beta - Validación y feedback

---

## 📞 Contacto y Soporte

### Información del Proyecto

* **Repositorio:** [https://github.com/JhoneiroLove/nutrialarm](https://github.com/JhoneiroLove/nutrialarm)
* **Issues:** [https://github.com/JhoneiroLove/nutrialarm/issues](https://github.com/JhoneiroLove/nutrialarm/issues)
* **Discusiones:** [https://github.com/JhoneiroLove/nutrialarm/discussions](https://github.com/JhoneiroLove/nutrialarm/discussions)

### Soporte al Usuario

* **Email:** [jhoneiro12@hotmail.com](mailto:soporte@nutrialarm.com)

### Redes Sociales

* **Instagram:** [@Jhoneiro](https://www.instagram.com/jhoneiro.java/)
* **LinkedIn:** [Jhoel Maqui](https://www.linkedin.com/in/jhoel-maqui-salda%C3%B1a-71b226267/)

---

<div align="center">
🍓 Alimentación saludable, vida saludable 🍓  
<strong>NutriAlarm - Tu compañero inteligente contra la anemia</strong>
</div>
