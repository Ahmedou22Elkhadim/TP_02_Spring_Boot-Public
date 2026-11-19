# TP 02 — Réponses aux questions de compréhension

## 1. IoC vs DI

- **IoC** (le principe) : ce n'est plus l'objet qui crée ses dépendances, c'est un conteneur externe (`ApplicationContext`).
- **DI** (le mécanisme) : Spring fournit à un bean les objets dont il a besoin, par constructeur, setter ou champ.

Avec `new CricketCoach()`, le contrôleur choisit et crée lui-même l'implémentation : il dépend d'elle.
Avec Spring, il déclare seulement « j'ai besoin d'un `Coach` », et c'est le conteneur qui choisit, crée et fournit l'objet.
Le contrôle de la création passe du code métier au conteneur.

## 2. Injection par constructeur plutôt que par champ

- **Immuabilité** : dépendances `final`.
- **Objet toujours valide** : l'objet ne peut pas exister sans ses dépendances, donc pas de `NullPointerException`.
- **Testabilité** : `new DemoController(mockCoach)` suffit, sans Spring ni réflexion.
- **Dépendances visibles** dans la signature du constructeur.

## 3. NoUniqueBeanDefinitionException

Elle apparaît quand plusieurs beans du même type peuvent être injectés et que rien n'indique lequel choisir
(5.2 : 4 implémentations de `Coach`, « found 4 »).

Il y a deux solutions :

- `@Qualifier("id")` au point d'injection ;
- `@Primary` sur une implémentation.

Si les deux sont présents, **`@Qualifier` gagne**. L'exercice 5.4 le montre : avec `@Primary` sur `TrackCoach`
et `@Qualifier("cricketCoach")`, la réponse est « Practice fast bowling ».

## 4. Id d'un bean

L'id par défaut est le nom de la classe avec la première lettre en minuscule (`CricketCoach` → `cricketCoach`).
Pour une méthode `@Bean`, c'est le nom de la méthode.

Pour forcer un id : `@Component("monCoach")` ou `@Bean("aquatic")`.

## 5. @Lazy

- **Avantages** : démarrage plus rapide, moins de mémoire utilisée.
- **Inconvénients** : les erreurs de configuration n'apparaissent qu'au premier appel, donc parfois en production,
  et la première requête est plus lente.
- **À utiliser avec parcimonie** : le démarrage eager garantit que le câblage est valide.
  On réserve `@Lazy` aux beans coûteux et peu utilisés.

## 6. /check : true puis false

J'ai ajouté `@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)` sur `CricketCoach`.

- `true` : en singleton, les deux injections reçoivent la même instance.
- `false` : en prototype, chaque injection crée une nouvelle instance.

Cela prouve que la portée détermine combien d'instances le conteneur crée.

## 7. Cycle de vie d'un singleton

Constructeur → injection des dépendances → `@PostConstruct` → utilisation → `@PreDestroy` (à l'arrêt du conteneur) → destruction.

Pour un prototype, Spring remet l'instance au demandeur sans garder de référence dessus.
Il ne la suit plus, donc il ne peut pas appeler `@PreDestroy`.

## 8. @Bean plutôt que @Component

1. Classe externe non modifiable (bibliothèque tierce), comme `SwimCoach`.
2. Création complexe ou logique de fabrique (factory).
3. Plusieurs instances du même type configurées différemment (deux `DataSource`, deux clients HTTP…).

## Tableau des scopes

| Scope              | Description                                                   | Contexte          |
|--------------------|---------------------------------------------------------------|-------------------|
| singleton (défaut) | Une seule instance dans le conteneur                          | Toute application |
| prototype          | Nouvelle instance à chaque demande ; `@PreDestroy` non appelé | Toute application |
| request            | Une instance par requête HTTP                                 | Web uniquement    |
| session            | Une instance par session HTTP                                 | Web uniquement    |
| application        | Une instance par ServletContext                               | Web uniquement    |