# Livestock Project Documentation

This directory contains the documentation for the Livestock project, created using [Slidev](https://sli.dev/).

## Getting Started

### Prerequisites

- Node.js (v14 or higher)
- npm or yarn

### Installation

```bash
# Install dependencies
npm install
# or
yarn install
```

### Development

To start the slide show in development mode:

```bash
npm run dev
# or
yarn dev
```

This will start a local server and open your default browser to view the slides. The slides will automatically reload when you make changes.

### Building for Production

To build the slides for production:

```bash
npm run build
# or
yarn build
```

This will generate a static site in the `dist` directory that you can deploy to any static hosting service.

### Exporting to PDF

To export the slides to PDF:

```bash
npm run export
# or
yarn export
```

## Customizing

### Slide Content

Edit the `slides.md` file to modify the content of the slides. Slidev uses Markdown with a special frontmatter section at the top for configuration.

### Themes

You can change the theme by modifying the `theme` property in the frontmatter of `slides.md`. Available themes include:
- `default`
- `seriph`
- And many more from the [Slidev themes collection](https://github.com/slidevjs/themes)

### Styling

You can customize the styling by creating a `style.css` file in this directory.

## Learn More

For more information about Slidev, check out the [Slidev documentation](https://sli.dev/).
