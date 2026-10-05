export default function Footer() {
  return (
    <footer className="site-footer">
      <div>
        <strong>MovieBook</strong>
        <p>Simple, reliable movie ticket booking.</p>
      </div>
      <p>© {new Date().getFullYear()} MovieBook</p>
    </footer>
  );
}
