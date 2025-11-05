# 📚 Consensus From Trust — Cleaned & Structured Repository

This repository was reorganized from your upload on 2025-11-05 18:21 into a clean, professional GitHub layout.
All original files are preserved under `original_upload_backup/`.

## What changed
- Moved **Python code** to `src/` and `tests/` (if present)
- Centralized **notebooks** under `notebooks/`
- Centralized **data** under `data/raw/` (never commit sensitive data)
- Placed **documentation** in `docs/` and images in `assets/images/`
- If present, standardized **Solidity/Hardhat** under `contracts/`
- If present, copied a frontend app under `dapp/`
- Added `.gitignore`, `LICENSE (MIT)`, and a minimal CI workflow

## Quick Start

### If Python code exists
```bash
python -m venv .venv && source .venv/bin/activate  # (Windows: .venv\Scripts\activate)
pip install -r requirements.txt  # placeholder; update as needed
python -m compileall src || true
```

### If Solidity contracts exist
```bash
cd contracts
npm install
cp .env.example .env
# set SEPOLIA_RPC_URL, PRIVATE_KEY (testnet)
npx hardhat compile
npx hardhat test
```

### If a frontend exists
```bash
cd dapp
npm install
npm run dev
```

> Educational scaffold; tailor requirements and CI as your project evolves.
