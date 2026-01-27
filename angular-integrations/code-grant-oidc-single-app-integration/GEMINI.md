# Gemini CLI Guidelines for Angular Projects

This document provides guidelines for the Gemini CLI to effectively work within this Angular project.

## 1. Project Structure

This is an Angular workspace. Key directories include:
- `projects/private-app`: Contains the main application.
- `projects/public-app`: Contains a public-facing application.
- `projects/shared-components`: Contains reusable components.

Always be mindful of the current working directory and which project context is relevant for the task at hand.

## 2. Common Commands

When performing tasks such as building, testing, linting, or serving, refer to `package.json` for available scripts.

- **Install Dependencies**:
  ```bash
  npm install
  ```
- **Serve an application (e.g., private-app)**:
  ```bash
  ng serve private-app
  ```
- **Build an application (e.g., private-app)**:
  ```bash
  ng build private-app
  ```
- **Run tests (e.g., private-app)**:
  ```bash
  ng test private-app
  ```
- **Run linting (e.g., private-app)**:
  ```bash
  ng lint private-app
  ```

If a specific project is not mentioned, assume the task applies to `private-app` by default.

## 3. Code Modifications and Conventions

- **Respect Angular CLI Best Practices**: When generating or modifying code, adhere to Angular CLI conventions (e.g., module structure, component declarations, service injection).
- **TypeScript**: Always use TypeScript, leveraging strong typing.
- **SCSS**: Prefer `.scss` for styling if existing components use it.
- **Existing Style**: Analyze surrounding code to match formatting, naming conventions, and architectural patterns.
- **Imports**: Ensure correct relative or absolute imports based on the existing codebase.
- **Testing**: When adding new features or fixing bugs, consider adding or updating `.spec.ts` files.

## 4. Troubleshooting

- If `ng` commands fail, ensure `npm install` has been run and check `package.json` for custom scripts.
- For build errors, `ng lint` and `ng build --configuration development` can provide more detailed output.
