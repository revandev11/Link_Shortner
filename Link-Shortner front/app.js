// Nginx proxies /api requests to the API Gateway, avoiding browser CORS issues.
const API_BASE_URL = '/api';

// The URL service may return a Docker-only host, so short links always use the host API Gateway.
function getPublicShortUrl(shortCode) {
  return `${window.location.origin}/${encodeURIComponent(shortCode)}`;
}

const form = document.querySelector('#shorten-form');
const urlInput = document.querySelector('#url-input');
const shortenButton = document.querySelector('#shorten-button');
const copyButton = document.querySelector('#copy-button');
const statsButton = document.querySelector('#stats-button');
const errorMessage = document.querySelector('#error-message');
const result = document.querySelector('#result');
const shortLink = document.querySelector('#short-link');
const statistics = document.querySelector('#statistics');
const totalClicks = document.querySelector('#total-clicks');
const createdEvents = document.querySelector('#created-events');

let currentShortCode = '';

function setLoading(isLoading) {
  shortenButton.disabled = isLoading;
  copyButton.disabled = isLoading;
  statsButton.disabled = isLoading;
}

function showError(message) {
  errorMessage.textContent = message;
  errorMessage.hidden = false;
}

function clearError() {
  errorMessage.textContent = '';
  errorMessage.hidden = true;
}

function getErrorText(data, fallback) {
  if (data && typeof data === 'object') {
    return Object.values(data).filter(Boolean).join(' ') || fallback;
  }
  return fallback;
}

async function readJson(response) {
  const text = await response.text();
  try {
    return text ? JSON.parse(text) : null;
  } catch {
    return null;
  }
}

form.addEventListener('submit', async (event) => {
  event.preventDefault();
  clearError();

  const url = urlInput.value.trim();
  if (!url) {
    showError('Please enter a long URL.');
    urlInput.focus();
    return;
  }

  setLoading(true);
  try {
    const response = await fetch(`${API_BASE_URL}/urls`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ url })
    });
    const data = await readJson(response);

    if (!response.ok) {
      throw new Error(getErrorText(data, 'The link could not be shortened.'));
    }

    if (!data?.shortUrl || !data?.shortCode) {
      throw new Error('The server returned an invalid short link response.');
    }

    currentShortCode = data.shortCode;
    const publicShortUrl = getPublicShortUrl(currentShortCode);
    shortLink.href = publicShortUrl;
    shortLink.textContent = publicShortUrl;
    result.hidden = false;
    statistics.hidden = true;
  } catch (error) {
    showError(error.message || 'A network error occurred. Check that the server is running.');
  } finally {
    setLoading(false);
  }
});

copyButton.addEventListener('click', async () => {
  clearError();
  try {
    await navigator.clipboard.writeText(shortLink.href);
    copyButton.textContent = 'Copied!';
    setTimeout(() => { copyButton.textContent = 'Copy'; }, 1800);
  } catch {
    showError('The link could not be copied.');
  }
});

statsButton.addEventListener('click', async () => {
  if (!currentShortCode) return;

  clearError();
  setLoading(true);
  try {
    const response = await fetch(
      `${API_BASE_URL}/analytics/${encodeURIComponent(currentShortCode)}`
    );
    const data = await readJson(response);

    if (!response.ok) {
      throw new Error(getErrorText(data, 'Statistics could not be retrieved.'));
    }

    totalClicks.textContent = data?.totalClicks ?? 0;
    createdEvents.textContent = data?.createdEvents ?? 0;
    statistics.hidden = false;
  } catch (error) {
    showError(error.message || 'A network error occurred. Check that the server is running.');
  } finally {
    setLoading(false);
  }
});
