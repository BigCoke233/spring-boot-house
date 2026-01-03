export function getImageUrl(path) {
    if (!path) return ''
    if (path.startsWith('http') || path.startsWith('https') || path.startsWith('blob:')) {
        return path
    }
    // Ensure path starts with /
    const normalizedPath = path.startsWith('/') ? path : `/${path}`
    return `http://localhost:8080${normalizedPath}`
}
