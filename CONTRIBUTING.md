# Contributing to AppDimens (SSPS)

First off, thank you for considering contributing to AppDimens\! It’s contributions like yours that make the Android developer experience better for everyone handling font scaling and layout consistency.

## How Can I Contribute?

### 1\. Reporting Bugs 🐛

If you find a font scaling error (sp), a layout calculation glitch, or a crash:

  * Check the [Issues](https://www.google.com/search?q=https://github.com/bodenberg/appdimens-ssps/issues) page to see if it has already been reported.
  * If not, open a new issue. Please include:
      * The **Android OS version** and **System Font Size** setting.
      * The device model or screen density (DPI).
      * A brief code snippet or XML layout showing how the library was used.

### 2\. Suggesting Enhancements 💡

Have an idea for better adaptive spacing or font interpolation?

  * Open an issue with the tag `enhancement`.
  * Explain the use case and how it benefits developers using the SSPS pattern.

### 3\. Pull Requests (PRs) 🚀

Ready to contribute code? Follow these steps:

1.  **Fork** the repository.
2.  Create a **Branch** for your feature or fix (`git checkout -b feature/amazing-feature`).
3.  Commit your changes with clear messages (e.g., `fix: adjust sp scaling for accessibility settings`).
4.  **Push** to the branch (`git push origin feature/amazing-feature`).
5.  Open a **Pull Request**.

## Technical Guidelines

  * **Code Style:** Follow standard Kotlin/Compose coding conventions.
  * **Precision:** Since this library focuses on typography and spacing, ensure that any changes to `sp` or `sdp` calculations are tested across multiple screen configurations and font scale settings.
  * **Performance:** Ensure that dimension resolution doesn't negatively impact UI thread performance in large lists.

## License

By contributing, you agree that your contributions will be licensed under the project's **Apache License 2.0**.
