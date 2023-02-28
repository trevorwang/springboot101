

## query 

1. Open  http://localhost:8080/graphiql?path=/graphql

2. Execute the below code in query

```graphql
query {
    recentPosts(count: 10, offset: 0) {
        id
        title
        category
        author {
            id
            name
            thumbnail
        }
    }
}
```
 
## mutation


```graphql
mutation CreatePostA($title: String!, $text: String!, $cate: String!, $author: String!) {
  createPost(title: $title, text: $text, category: $cate, authorId: $author) {
    text
    title
    author {
      name
      id
    }
  }
}

```
variables

```json
{
  "title": "12313",
  "text": "xfdsfs",
  "cate": "xxxxx",
  "author": "Author0"
}
```