# Développement Android UnivMobile

### Environnement de développement

Pour installer le projet, l’environnement de développement doit disposer de :

  * un JDK 6 ou supérieur
  * un Android SDK avec au moins la target android-19
  * un fichier « local.properties »

Pour vérifier la version du JDK : java -version

Exemple :

    $ java -version
    java version "1.7.0_17"
    Java(TM) SE Runtime Environment (build 1.7.0_17-b02)
    Java HotSpot(TM) 64-Bit Server VM (build 23.7-b01, mixed mode)

Pour vérifier que la target android-19 est installée avec l’Android SDK : android list targets

Exemple :

    $ /opt/android-sdk-mac/tools/android list targets
    Available Android targets:
    ----------
    (...)
    id: 1 or "android-19"
         Name: Android 4.4.2
         Type: Platform
         API level: 19
         Revision: 3
         Skins: HVGA, QVGA, WQVGA400, WQVGA432, WSVGA, WVGA800 (default), WVGA854, WXGA720, WXGA800, WXGA800-7in
     Tag/ABIs : default/armeabi-v7a, default/x86
    (...)

Le fichier « local.properties » doit être copié dans UnivMobile et dans google-play-services/google-play-services_lib/

Les différents fichiers « local.properties » ne seront pas stockés dans GitHub. Des fichiers sont fournis en exemple — eux sont stockés dans GitHub : « local.properties_jenkins », etc. Chaque fichier « local.properties » doit notamment contenir l’emplacement de l’Android SDK. Par exemple :
  
    sdk.dir=/opt/android-sdk-linux
    
Attention, sur Mac, le nom du répertoire d’installation de l’Android SDK, « /Applications/Android Studio.app/sdk » contient une espace. Il vaut mieux donner un nom sans espace, par exemple en faisant un lien symbolique :

    $ sudo ln -s /Applications/Android\ Studio.app/sdk /opt/android-sdk-mac
    
### IDEs

Le projet peut être développé avec Eclipse ADT ou Android Studio.

#### Eclipse ADT

Le projet « UnivMobile » peut être ouvert tel quel.

Les fichiers .project et .classpath sont dans GitHub, pas le répertoire .settings/.

#### Android Studio

Le projet « UnivMobile » peut être ouvert tel quel, en tant que projet Gradle. Attention a bien spécifier :

  * Gradle project: (…)/unm-android/UnivMobile
  * _et non : (...)/unm-android/UnivMobile/gradle_

Les fichier et répertoire UnivMobile.iml et .idea/ ne sont pas dans GitHub.

### En ligne de commande 

#### Avec Ant (= Eclipse ADT)

Ant doit être installé.

Configuration testée : Ant 1.8.2, JVM 1.7.0_17

    $ cd UnivMobile/
    $ ant clean-all release
    $ ant clean-classes debug

Les fichiers résultants sont :

    ./bin/UnivMobile-debug.apk
    ./bin/UnivMobile-release-unsigned.apk
    
#### Avec Gradle (= Android Studio)
  
Gradle doit être installé.

Configuration testée : Gradle 2.0, JVM 1.7.0_17

    $ cd UnivMobile/
    $ ./gradlew wrapper
    $ ./gradlew clean build

Les fichiers résultants sont :

    ./build/outputs/apk/UnivMobile-debug-unaligned.apk
    ./build/outputs/apk/UnivMobile-debug.apk		
    ./build/outputs/apk/UnivMobile-release-unsigned.apk  
        
Note : les répertoires libs/ et ../google-play-services/ ne sont pas utilisés par Gradle (gestion de dépendances).

