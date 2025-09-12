import { LitElement, html, css } from 'lit';
import { customElement } from 'lit/decorators.js';

@customElement('loading-message-lit')
export class LoadingMessageLit extends LitElement {
  static styles = css`
    :host {
      display: block;
    }
    
    .loading-message {
      text-align: center;
      padding: 40px 20px;
      background-color: #1e2329;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      transition: all 0.3s ease;
    }
    
    h3 {
      color: #f0b90b;
      margin: 16px 0 8px;
      font-weight: 500;
    }
    
    p {
      color: #848e9c;
      font-style: italic;
      margin-bottom: 0;
    }
    
    .spinner {
      width: 50px;
      height: 50px;
      border: 3px solid rgba(240, 185, 11, 0.2);
      border-radius: 50%;
      border-top-color: #f0b90b;
      animation: spin 1s ease-in-out infinite;
      margin-bottom: 16px;
    }
    
    @keyframes spin {
      to {
        transform: rotate(360deg);
      }
    }
  `;

  render() {
    return html`
      <div class="loading-message">
        <div class="spinner"></div>
        <h3>Loading Cryptocurrency Data</h3>
        <p>Fetching the latest market updates...</p>
      </div>
    `;
  }
}

declare global {
  interface HTMLElementTagNameMap {
    'loading-message-lit': LoadingMessageLit;
  }
}
