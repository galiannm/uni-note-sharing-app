import { ColorSchemeScript } from '@mantine/core';
import '@mantine/core/styles.css';
import { Metadata } from 'next';
import Providers from './providers';

export const metadata: Metadata = {
  title: 'HotaNotes',
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en" data-mantine-color-scheme="light">
      <head>
        <ColorSchemeScript />
      </head>
      <body>
        <Providers>{children}</Providers>
      </body>
    </html>
  );
}
