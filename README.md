# Sistema de Gestão de Estoque 📦

Este é um aplicativo Android moderno para gestão de estoque, que combina a rapidez de um banco de dados local com a facilidade de sincronização em tempo real via nuvem.

## 🚀 Ideia do App
A ideia principal é oferecer uma ferramenta simples e eficiente para pequenos lojistas ou usuários que precisam controlar mercadorias. O diferencial é o **sistema de "Código da Loja"**, que permite que diferentes pessoas (como funcionários de uma mesma loja) compartilhem e editem o mesmo estoque simultaneamente, apenas inserindo o mesmo código.

## ✨ Funcionalidades
- **Sincronização em Tempo Real**: Usa Firebase Firestore para que qualquer alteração em um dispositivo reflita instantaneamente em outros.
- **Modo Offline (Room)**: Os dados ficam salvos localmente, permitindo consulta mesmo sem internet. O app se sincroniza automaticamente ao reconectar.
- **Dashboard de Valor**: Calcula automaticamente o valor total do estoque (Preço x Quantidade).
- **Flexibilidade de Unidades**: Suporta cadastro de produtos por **Unidade (UN)** ou **Quilograma (KG)**.
- **Busca Rápida**: Filtro inteligente para encontrar produtos pelo nome.
- **Privacidade por Código**: Sistema de IDs de loja customizáveis para isolar os dados entre diferentes grupos de usuários.

## 🛠️ Tecnologias Utilizadas
- **Linguagem**: Java
- **Banco Local**: Room Persistence Library (Android Jetpack)
- **Nuvem/Backend**: Firebase Firestore & Firebase Authentication
- **Interface**: Material Design 3, RecyclerView, ConstraintLayout

## 📱 Como usar
1. Abra o app e cadastre seus primeiros produtos.
2. Clique no ícone de ferramenta (configuração) no topo para definir um **Código de Loja** único.
3. Peça para outros usuários inserirem o mesmo código no app deles.
4. Pronto! Todos agora compartilham o mesmo estoque em tempo real.

---
Desenvolvido por [Erick Gustavo](https://github.com/erickgstv)