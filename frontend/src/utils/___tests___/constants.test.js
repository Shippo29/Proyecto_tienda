import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest'

describe('constants', () => {
  const originalGatewayUrl = import.meta.env.VITE_API_GATEWAY_URL
  const originalTimeout = import.meta.env.VITE_API_TIMEOUT

  beforeEach(() => {
    vi.resetModules()
  })

  afterEach(() => {
    import.meta.env.VITE_API_GATEWAY_URL = originalGatewayUrl
    import.meta.env.VITE_API_TIMEOUT = originalTimeout
  })

  it('usa los valores por defecto cuando no hay variables de entorno definidas', async () => {
    import.meta.env.VITE_API_GATEWAY_URL = ''
    import.meta.env.VITE_API_TIMEOUT = ''

    const { API_BASE_URL, API_TIMEOUT } = await import('../../../src/utils/constants')

    expect(API_BASE_URL).toBe('http://localhost:8080')
    expect(API_TIMEOUT).toBe(10000)
  })

  it('usa los valores de las variables de entorno cuando estan definidas', async () => {
    import.meta.env.VITE_API_GATEWAY_URL = 'https://api.mitienda.com'
    import.meta.env.VITE_API_TIMEOUT = '5000'

    const { API_BASE_URL, API_TIMEOUT } = await import('../../../src/utils/constants')

    expect(API_BASE_URL).toBe('https://api.mitienda.com')
    expect(API_TIMEOUT).toBe(5000)
  })
})
