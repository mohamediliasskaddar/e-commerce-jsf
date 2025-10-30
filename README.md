# Atelier Application E-Commerce avec JSF et JPA

## 📋 Description du Projet
Cet atelier consiste en la réalisation d'une application web de commerce électronique utilisant les technologies **JSF 4.0** et **JPA 3.0**. L'application simule le comportement d'un site e-commerce avec gestion des utilisateurs, produits, commandes et panier d'achat.

## 🎯 Objectifs Pédagogiques
- Maîtriser l'API JPA (Java Persistence API) pour la persistance des données
- Comprendre et utiliser le framework JSF pour le développement d'interfaces web
- Implémenter une architecture MVC avec CDI
- Développer une application web complète avec gestion des transactions

## 🛠️ Environnement Technique

### Technologies Utilisées
- **Backend**: Java EE, JPA 3.0, CDI
- **Frontend**: JSF 4.0, Facelets (XHTML), CSS
- **Base de Données**: MySQL avec connecteur JDBC
- **Serveur d'Application**: WildFly
- **ORM**: Hibernate/EclipseLink
- **UI Components**: PrimeFaces
- **IDE**: IntelliJ IDEA
- **Gestion de Build**: Maven

### Prérequis
- Java JDK 11 ou supérieur
- Maven 3.6+
- WildFly 26+
- MySQL 8.0+
- IntelliJ IDEA

## 📊 Architecture de l'Application

### Diagramme de Classes

![Diagramme de Classes](screenshots/homepage.png)


### Entités JPA Principales
- **User**: Gestion des utilisateurs (Admin/Client)
- **Product**: Catalogue des produits
- **Order**: Commandes des utilisateurs
- **CommandLine**: Lignes de commande associant produits et quantités

## 🚀 Étapes de Développement

### Étape 1: Modélisation et Diagramme de Classes
Conception du modèle de données centré sur:
- Gestion du panier d'achat
- Vitrine des produits
- Profil internaute/client

### Étape 2: Configuration du Projet
```xml
<!-- pom.xml -->
<dependencies>
    <dependency>
        <groupId>jakarta.platform</groupId>
        <artifactId>jakarta.jakartaee-web-api</artifactId>
        <version>9.1.0</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>org.eclipse.persistence</groupId>
        <artifactId>eclipselink</artifactId>
        <version>3.0.0</version>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
    <dependency>
        <groupId>org.primefaces</groupId>
        <artifactId>primefaces</artifactId>
        <version>12.0.0</version>
    </dependency>
</dependencies>
```

### Étape 3: Couche de Persistance JPA

#### Configuration persistence.xml
```xml
<persistence-unit name="ecommercePU" transaction-type="JTA">
    <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
    <jta-data-source>java:/MySqlDS</jta-data-source>
    <class>ma.fstt.ecommerce1.model.User</class>
    <class>ma.fstt.ecommerce1.model.Product</class>
    <class>ma.fstt.ecommerce1.model.Order</class>
    <class>ma.fstt.ecommerce1.model.CommandLine</class>
    <properties>
        <property name="jakarta.persistence.schema-generation.database.action" value="create"/>
        <property name="eclipselink.logging.level" value="FINE"/>
    </properties>
</persistence-unit>
```

#### Exemple d'Entité JPA
```java
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_user;
    
    private String username;
    private String email;
    private String password;
    private String role;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();
}
```

### Étape 4: Couche Présentation JSF

#### Backing Beans avec CDI
```java
@Named
@SessionScoped
public class UserC implements Serializable {
    @Inject
    private UserDAO userDAO;
    
    private User user;
    private List<User> users;
    
    // Méthodes de gestion utilisateur
}
```




## 📁 Structure du Projet
```
src/
├── main/
│   ├── java/
│   │   └── ma/fstt/ecommerce1/
│   │       ├── controllers/     # Backing Beans CDI
│   │       ├── dao/            # Data Access Objects
│   │       ├── model/          # Entités JPA
│   │       └── utils/       # Logique métier
│   ├── resources/
│   │   └── META-INF/
│   │       └── persistence.xml # Configuration JPA
│   └── webapp/
│       ├── WEB-INF/
│       │   └── web.xml        # Configuration JSF
│       ├── resources/         # CSS, JS, images
│       └── views/             # Pages XHTML
│           ├── products/
│           ├── orders/
│           ├── users/
│           └── includes/
```

## 🎮 Fonctionnalités Implémentées

### Pour les Clients
- ✅ Inscription et authentification
- ✅ Consultation du catalogue produits
- ✅ Gestion du panier d'achat
- ✅ Passer des commandes
- ✅ Consultation de l'historique des commandes

### Pour les Administrateurs
- ✅ Gestion complète des produits
- ✅ Visualisation de toutes les commandes
- ✅ Gestion des utilisateurs
- ✅ Tableaux de bord statistiques

### Features Techniques
- ✅ Interface responsive avec PrimeFaces
- ✅ Navigation dynamique avec AJAX
- ✅ Gestion des transactions JTA
- ✅ Validation des données
- ✅ Gestion des erreurs

## 🔧 Installation et Déploiement

### 1. Configuration de la Base de Données
```sql
CREATE DATABASE ecommerce_jsf;
CREATE USER 'ecommerce_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON ecommerce_db.* TO 'ecommerce_user'@'localhost';
```

### 2. Configuration WildFly
```xml
<!-- standalone.xml -->
<datasource jta="true" jndi-name="java:/MySqlDS" pool-name="MySqlDS">
    <connection-url>jdbc:mysql://localhost:3306/ecommerce_jsf</connection-url>
    <driver>mysql</driver>
    <security>
        <user-name>ecommerce_user</user-name>
        <password>password</password>
    </security>
</datasource>
```

### 3. Déploiement
```bash
mvn clean package wildfly:deploy
```

## 📸 Captures d'Écran

<!-- Ajoutez vos captures d'écran ici -->
### Page d'Accueil
![Page d'Accueil](screenshots/homepage.png)

### Gestion des Produits
![Gestion Produits](screenshots/products-management.png)

### Panier d'Achat
![Panier](screenshots/shopping-cart.png)

### Commandes
![Commandes](screenshots/orders.png)

### Interface d'Administration
![Admin](screenshots/admin-dashboard.png)

## 🧪 Tests et Validation

### Scénarios Testés
- [x] Création de compte utilisateur
- [x] Authentification et gestion de session
- [x] Ajout/suppression de produits au panier
- [x] Validation des commandes
- [x] Calcul automatique des totaux
- [x] Persistance des données
- [x] Gestion des rôles (Admin/Client)


## 👥 Rôles et Permissions

| Fonctionnalité | Client | Admin |
|----------------|--------|-------|
| Voir produits  | ✅     | ✅    |
| Gérer panier   | ✅     | ❌    |
| Passer commande| ✅     | ❌    |
| Gérer produits | ❌     | ✅    |
| Voir toutes commandes | ❌ | ✅ |
| Gérer utilisateurs | ❌ | ✅ |

## 💡 Points Techniques Importants

### Gestion des Transactions
```java
@Transactional
public Order createOrder(Order order) {
    // Logique métier avec gestion transactionnelle
}
```

### Navigation JSF
```java
public String createOrder() {
    // Validation et traitement
    return "/views/orders/confirmation?faces-redirect=true";
}
```

### Composants PrimeFaces
```xhtml
<p:commandButton value="Ajouter" 
                 action="#{cartC.addItem}" 
                 update="cartTable" 
                 ajax="true"/>
```

## 📚 Ressources et Références

- [Documentation JSF 4.0](https://jakarta.ee/specifications/faces/4.0/)
- [Documentation JPA 3.0](https://jakarta.ee/specifications/persistence/3.0/)
- [PrimeFaces Showcase](https://www.primefaces.org/showcase/)
- [WildFly Documentation](https://www.wildfly.org/documentation/)

---

**Réalisé par:** Kaddar Mohamed iliass  
**Date:** 26/10/2025  
