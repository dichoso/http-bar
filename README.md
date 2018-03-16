# Online Bar

## API rest

```
/menu
    get returns [ { name, price } ]

/command
    post { address, command } returns erreur | "votre thé va etre servi à (lat, log)"
```