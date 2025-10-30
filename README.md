
---

# E-Commerce Web Application (JSF 4.0 & JPA 3.0)

## Résumé du Projet

**Description Générale**
Développement d'une application web e-commerce complète utilisant **JSF 4.0** et **JPA 3.0** avec une architecture **MVC**.
L'application simule une plateforme de vente en ligne avec gestion des utilisateurs, produits, commandes et panier d'achat.

---

## Technologies Utilisées

| Couche              | Technologies                                   |
| ------------------- | ---------------------------------------------- |
| **Backend**         | Java EE, JPA 3.0, CDI, Hibernate / EclipseLink |
| **Frontend**        | JSF 4.0, Facelets (XHTML), PrimeFaces, CSS3    |
| **Base de Données** | MySQL 8.0                                      |
| **Serveur**         | WildFly 37                                     |
| **Outils**          | IntelliJ IDEA, Maven                           |

---

## Architecture et Conception

### Modèle de Données

```java
// Entités principales
User (id, username, email, password, role)
Product (id, name, price, stock)
Order (id, date, total, user)
CommandLine (id, quantity, order, product)
```

### Design Patterns Implémentés

* **MVC (Model-View-Controller)** avec JSF
* **DAO (Data Access Object)** pour l’abstraction de la persistance
* **CDI (Contexts and Dependency Injection)** pour l’injection de dépendances
* **Session Façade** pour la gestion des transactions

---

## Fonctionnalités Implémentées

### Module Authentification

* Inscription avec validation des données
* Connexion / Déconnexion sécurisée
* Gestion des rôles (**Admin / Client**)
* **Pages :** `Login.xhtml`, `Register.xhtml`

### Module Produits

* CRUD complet des produits
* Interface administrateur pour la gestion
* Affichage en grille avec design en cartes
* **Pages :** `ProductsList.xhtml`, `AddProduct.xhtml`, `EditProduct.xhtml`

### Module Commandes

* Création de commandes avec panier dynamique
* Gestion des lignes de commande en temps réel
* Calcul automatique des totaux
* Historique des commandes par utilisateur
* Interface admin avec vue globale
* **Pages :** `OrderCreation.xhtml`, `MyOrders.xhtml`, `AllOrders.xhtml`

### Module Utilisateurs

* Gestion des profils et rôles
* Séparation des permissions
* Interface admin pour la gestion des utilisateurs

---

## Interface Utilisateur

| Page                  | Description                        |
| --------------------- | ---------------------------------- |
| **Page d’Accueil**    | Design moderne avec dégradé        |
| **Authentification**  | Formulaires centrés et responsives |
| **Gestion Produits**  | Grille de cartes interactives      |
| **Commandes**         | Interface accordéon avec détails   |
| **Création Commande** | Tableau dynamique avec AJAX        |

---

## Aspects Techniques

### Configuration JPA

```xml
<persistence-unit name="ecommercePU">
  <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
  <jta-data-source>java:/MySqlDS</jta-data-source>
  <properties>
    <property name="jakarta.persistence.schema-generation.database.action" value="create"/>
  </properties>
</persistence-unit>
```

### Gestion des Sessions

```java
@Named
@SessionScoped
public class UserC implements Serializable {
    // Gestion de l'utilisateur connecté
}
```

### Transactions JTA

```java
@Transactional
public Order createOrder(Order order) {
    // Logique métier transactionnelle
}
```

---

## Fonctionnalités Avancées

### AJAX et Interactivité

* Mise à jour dynamique des sous-totaux
* Ajout / Suppression de lignes sans rechargement
* Affichage / Masquage des détails de commande
* Validation en temps réel

### Gestion d’État

* **Scope Session** pour les données utilisateur
* **Scope Request** pour les DAOs
* Gestion persistante du panier

### Sécurité

* Contrôle d’accès basé sur les rôles
* Validation côté serveur
* Gestion des erreurs utilisateur

---

## Objectifs Atteints

* Conformité aux exigences du projet e-commerce
* Architecture modulaire : **JSF / JPA / CDI**
* Couche persistance JPA avec entités
* Backing Beans et composants XHTML intégrés
* Intégration complète de **PrimeFaces**
* Rapport technique détaillé

---

## Fonctionnalités Clés

* Gestion complète du cycle de commande
* Interface moderne et responsive
* Expérience utilisateur fluide
* Code maintenable, structuré et extensible

---

**Projet développé dans un cadre académique avec une architecture professionnelle respectant les bonnes pratiques du développement Java EE.**

---


