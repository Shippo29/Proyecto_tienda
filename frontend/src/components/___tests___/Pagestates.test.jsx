import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import {
LoadingState,
ErrorState,
EmptyState,
PageStateContainer,
} from '../../../src/components/common/PageStates'

describe('LoadingState', () => {
    it('muestra mensaje de carga por defecto', () => {
    render(<LoadingState />)
    expect(screen.getByText('Cargando...')).toBeInTheDocument()
    })

    it('muestra mensaje personalizado', () => {
    render(<LoadingState message="Procesando datos..." />)
    expect(screen.getByText('Procesando datos...')).toBeInTheDocument()
    })
})

describe('ErrorState', () => {
    it('muestra el mensaje de error', () => {
    render(<ErrorState message="Error al cargar" />)
    expect(screen.getByText(/Error al cargar/)).toBeInTheDocument()
    })

    it('muestra boton reintentar cuando se pasa onRetry', () => {
    const onRetry = vi.fn()
    render(<ErrorState message="Error" onRetry={onRetry} />)
    expect(screen.getByText('Reintentar')).toBeInTheDocument()
    })

    it('llama onRetry al hacer click en reintentar', () => {
    const onRetry = vi.fn()
    render(<ErrorState message="Error" onRetry={onRetry} />)
    fireEvent.click(screen.getByText('Reintentar'))
    expect(onRetry).toHaveBeenCalledTimes(1)
    })

    it('no muestra boton reintentar si no se pasa onRetry', () => {
    render(<ErrorState message="Error" />)
    expect(screen.queryByText('Reintentar')).not.toBeInTheDocument()
    })
})

describe('EmptyState', () => {
    it('muestra mensaje por defecto', () => {
    render(<EmptyState />)
    expect(screen.getByText('No hay datos disponibles')).toBeInTheDocument()
    })

    it('muestra mensaje personalizado', () => {
    render(<EmptyState message="No hay productos" />)
    expect(screen.getByText('No hay productos')).toBeInTheDocument()
    })
})

describe('PageStateContainer', () => {
    it('muestra loading cuando loading es true', () => {
    render(<PageStateContainer loading={true}><div>contenido</div></PageStateContainer>)
    expect(screen.getByText('Cargando...')).toBeInTheDocument()
    expect(screen.queryByText('contenido')).not.toBeInTheDocument()
    })

    it('muestra error cuando error tiene valor', () => {
    render(<PageStateContainer loading={false} error="Error de red"><div>contenido</div></PageStateContainer>)
    expect(screen.getByText(/Error de red/)).toBeInTheDocument()
    expect(screen.queryByText('contenido')).not.toBeInTheDocument()
    })

    it('muestra empty cuando isEmpty es true', () => {
    render(<PageStateContainer loading={false} error={null} isEmpty={true}><div>contenido</div></PageStateContainer>)
    expect(screen.getByText('No hay datos disponibles')).toBeInTheDocument()
    })

    it('muestra children cuando no hay estado especial', () => {
    render(<PageStateContainer loading={false} error={null} isEmpty={false}><div>contenido</div></PageStateContainer>)
    expect(screen.getByText('contenido')).toBeInTheDocument()
    })
})