# AppDimens SSP, HSP, WSP

![AppDimens Banner](IMAGES/banner_top.png)

Bem-vindo à documentação oficial da biblioteca **AppDimens SSPS**.

## 📖 O que é a biblioteca?

A **AppDimens SSP, HSP, WSP** é um sistema moderno de gerenciamento de dimensões exclusivo para tipografia e fontes (`Sp`) no Android. Ela expande o padrão clássico do SSP (Scaled Size Pixels) introduzindo também o dimensionamento por Altura (HSP) e Largura (WSP). A biblioteca automatiza o processo de ajuste dos tamanhos de texto (`TextUnit`), assegurando que a tipografia permaneça perfeitamente escalada e legível em qualquer formato de dispositivo de maneira matematicamente precisa.

## ⚙️ O que ela faz?

Ela fornece milhares de recursos `@dimen` pré-calculados (de `1` a `600`) prontos para usar, poupando ao desenvolvedor o trabalho de calcular tamanhos de fonte para cada variante de tela de Android.

* **SSP (Smallest Width SP):** Escala a fonte com base na menor largura (Smallest Width) disponível do dispositivo. Perfeito para manter a proporção do texto na maioria dos cenários (ex: `@dimen/_16ssp` ou `16.ssp`).
* **WSP (Width SP):** Escala o texto fundamentado especificamente na largura horizontal exata do dispositivo na orientação atual (ex: `@dimen/_16wsp` ou `16.wsp`).
* **HSP (Height SP):** Escala o texto fundamentado especificamente na altura vertical exata do dispositivo (ex: `@dimen/_16hsp` ou `16.hsp`).
* **SEM, WEM, HEM (Ignorar Escala da Fonte):** Variantes `.sem`, `.wem`, `.hem` funcionam da mesma forma que os recursos padrões SSP/WSP/HSP, mas **não acompanham as configurações de acessibilidade de escala de fonte do sistema**. São úteis para textos que não devem quebrar o design de componentes estritos independente da acessibilidade do usuário.
* **Condicionais Dinâmicas (Compose):** Facilita a adaptação da fonte baseada no tipo de dispositivo (Carro, TV, Relógio) através da instrução `.scaledSp()`.

<br/>
<p align="center">
  <img src="IMAGES/screenshot.png" alt="Exemplo do layout" width="25%" />
</p>
<br/>

## 🚀 Vantagens

1. **Desenvolvimento Acelerado:** Elimina a necessidade de criar arquivos `dimens.xml` manuais massivos para várias categorias de telas (como `values-sw320dp`, `values-sw600dp`). Tudo já vem unificado.
2. **Integração Híbrida Direta:** Funciona incrivelmente bem tanto no tradicional **XML** (`View System`) por meio de dimensões predefinidas, quanto na era moderna do **Jetpack Compose**.
3. **Escalonamento Flexível:** Permite customizar a tipografia controlando se as escalas de acessibilidade do usuário do Android devem ou não afetar certos textos, por meio de `.ssp` (que respeita) vs `.sem` (que ignora a escala do usuário).
4. **Precisão para TV, Wear OS e Auto:** Trata regras avançadas de fontes sem complexidade usando `UiModeType` combinados a qualificadores.

## ⚡ Performance

A implementação garante um impacto zero ou virtualmente nulo na performance:
* **No XML:** Todas as tags como `@dimen/_16ssp` são processadas estaticamente no build time e resolvidas de forma nativa e paralela aos recursos do Android Framework.
* **No Compose:** O acesso a `.ssp`, `.hsp` e `.wsp` usa funções otimizadas que extraem as dimensões via context caching nativo (`LocalConfiguration`, `LocalDensity` e IDs injetados). Evitando o processamento desnecessário, ela respeita as etapas convencionais da UI sem forçar recomposições inúteis.

## 🛠️ Suporte e Instalação

A biblioteca tem amplo suporte no ecossistema Android e é constantemente acompanhada para os paradigmas mais recém-lançados.

* **Min SDK:** 24
* **Compile SDK:** 36
* **Linguagens:** Kotlin e Java.
* **Paradigma:** XML e Jetpack Compose.

Para instalar, basta adicionar no seu `build.gradle` (dependência):

```kotlin
dependencies {
    implementation("io.github.bodenberg:appdimens-ssps:3.0.0")
}
```

### Exemplo Rápido no Compose:
```kotlin
Text(
    text = "Dimensionamento Responsivo",
    fontSize = 24.ssp, // Escala a fonte com base no Smallest Width e respeita a escala de fonte do sistema
    lineHeight = 28.ssp
)

Text(
    text = "Tamanho de Texto Restrito",
    fontSize = 16.sem // Escala baseada no Smallest Width, mas NÃO é afetado pela preferência de acessibilidade de visão
)
```

### Exemplo Condicional Avançado:
```kotlin
val dynamicFontSize = 16.sp.scaledSp()
    .screen(UiModeType.TELEVISION, customValue = 32.ssp)
    .ssp // Resultado: 32.ssp na TV, 16.ssp nos demais aparelhos móveis
```

![Demonstração extra](IMAGES/image.png)

---
*Criado com as melhores práticas de layout responsivo e acessível para o ecossistema Android.*
