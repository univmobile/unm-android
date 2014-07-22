# Développement Android UnivMobile

### Environnement de développement

Pour installer le projet, l’environnement de développement doit disposer de :

  * un JDK 6 ou supérieur,
  * un Android SDK,
  * un fichier « local.properties ».
  
Le fichier « local.properties » doit être copié dans UnivMobile et dans google-play-services/google-play-services_lib/

Les différents fichiers « local.properties » ne seront pas stockés dans GitHub. Des fichiers sont fournis en exemple — eux sont stockés dans GitHub : « local.properties_jenkins », etc. Chaque fichier « local.properties » doit notamment contenir l’emplacement de l’Android SDK. Par exemple :
  
    sdk.dir=/opt/android-sdk-linux
    
Attention, sur Mac, le nom du répertoire d’installation de l’Android SDK, « /Applications/Android Studio.app/sdk » contient une espace. Il vaut mieux donner un nom sans espace, par exemple en faisant un lien symbolique :

    $ sudo ln -s /Applications/Android\ Studio.app/sdk /opt/android-sdk-mac
    
### IDE

Le projet peut être développé avec Eclipse ADT ou Android Studio.

### Ligne de commande 

#### Avec Ant (= Eclipse ADT)

Ant doit être installé.

Configuration testée : Ant 1.8.2, JVM 1.7.0_17

    $ cd UnivMobile/
    $ ant clean-all release
    $ ant clean-classes debug
    
#### Avec Gradle (= Android Studio)
  
Gradle doit être installé.

Configuration testée : Gradle 2.0, JVM 1.7.0_17

    $ cd UnivMobile/
    $ ./gradlew wrapper
    $ ./gradlew clean build
  
Note : les répertoires libs/ et ../google-play-services/ ne sont pas utilisés par Gradle (gestion de dépendances).

