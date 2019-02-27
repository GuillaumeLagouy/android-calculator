# Application de News sous Android 🤖

## Sommaire
1. [Conception](#conception)
2. [Organisation](#conception)
3. [Installation](#conception)
4. [Demo](#conception)

## <a name="conception">1. Conception</a>

### Fonctionnalités :
#### **🔁 News en continues grâce à [NewsAPI](https://newsapi.org/)**

Cet API fournit un flux continu de news depuis 30,000 sources de news et blog.
> 👎 Cette API ne fournit qu'une petite partie de la news pour l'avoir en entière il faut suivre le lien.

> 👍 Chaque article est fournit avec une image

Grâce à ça le contenu de l'application ressemblera à une véritable appli de news. Avec des mises à jour en temps réel etc ...

Un exemple de retour :

```json
source": {
  "id": "techcrunch",
  "name": "TechCrunch"
},
  "author": "Sarah Perez",
  "title": "Affirm’s latest partnership brings its alternative financing to Walmart’s U.S. stores and website",
  "description": "Financial technology company Affirm, which offers consumers an alternative to cash and credit when paying for large purchases, has scored a notable new partner: Walmart. The companies announced this morning that Affirm’s financing options would be made availa…",
  "url": "https://techcrunch.com/2019/02/27/affirms-latest-partnership-brings-its-alternative-financing-to-walmarts-u-s-stores-and-website/",
  "urlToImage": "https://techcrunch.com/wp-content/uploads/2018/01/gettyimages-460935470.jpg?w=585",
  "publishedAt": "2019-02-27T16:54:27Z",
  "content": "Financial technology company Affirm, which offers consumers an alternative to cash and credit when paying for large purchases, has scored a notable new partner: Walmart. The companies announced this morning that Affirm’s financing options would be made availa… [+3100 chars]"
```

Requête HTTP grâce à [Retrofit](https://square.github.io/retrofit/)

---

#### **🔎 Rechercher des news par mot-clé**

Recherche grâce à une [SearchView](https://developer.android.com/reference/android/widget/SearchView) dans le Layout. Puis dans le fragment
```java
public void search(View view){
    SearchView searchView = view.findViewById(R.id.search);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            model.loadNews(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    });
}
```

---

#### **📴 Consulter les news hors-connexion**

L'architecture de l'application est basé sur celui préconisé par [Android](https://developer.android.com/topic/libraries/architecture/index.html) :

![Architecture Android](https://codelabs.developers.google.com/codelabs/android-room-with-a-view/img/3840395bfb3980b8.png)

Pour pouvoir consulter les news hors-connexion elles sont insérées dans une base de donnée [SQLite](https://sqlite.org/index.html) quand il y a de la connexion. Elles sont récupérer dans la base donnée en l'absence de connexion.

---

#### **⭐ Mettre en favoris une news**

> 🛠️En cours

## <a name="organisation">2. Organisation</a>

Je me suis occupé de cette application pendant que [Hash7](https://github.com/Hashs7) développait la calculette.

## <a name="installation">3. Installation</a>

> Cf le [README](https://github.com/GuillaumeLagouy/android-calculator/blob/master/README.md) du projet

## <a name="demo">4. Demo</a>

- Feed de news

![news feed](https://res.cloudinary.com/dcqc12ai5/image/upload/c_scale,h_563,q_67/v1551292327/news_feed.gif)

- Recherche

<img src="https://res.cloudinary.com/dcqc12ai5/image/upload/v1551293856/search.gif" alt="drawing" width="317"/>

- Hors-connexion

![hors-connexion](https://res.cloudinary.com/dcqc12ai5/image/upload/c_scale,h_563,q_67/v1551292509/no_connexion.gif)
